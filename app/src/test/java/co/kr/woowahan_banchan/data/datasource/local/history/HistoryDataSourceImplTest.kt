package co.kr.woowahan_banchan.data.datasource.local.history

import co.kr.woowahan_banchan.data.database.dao.HistoryDao
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.InterruptedIOException
import java.nio.channels.InterruptedByTimeoutException

@ExperimentalCoroutinesApi
class HistoryDataSourceImplTest {
    private val historyDto1 = HistoryDto("A", 0, "item0")
    private val historyDto2 = HistoryDto("B", 1, "item1")
    private val historyDto3 = HistoryDto("C", 2, "item2")
    private val historyDto4 = HistoryDto("D", 3, "item3")
    private val historyDto5 = HistoryDto("E", 4, "item4")
    private val historyDto6 = HistoryDto("F", 5, "item5")
    private val historyDto7 = HistoryDto("G", 6, "item6")
    private val historyDto8 = HistoryDto("H", 7, "item7")
    private val originalHistoryDtos = listOf(
        historyDto1,
        historyDto2,
        historyDto3,
        historyDto4,
        historyDto5,
        historyDto6,
        historyDto7,
        historyDto8
    )

    private lateinit var historyDao: FakeHistoryDao
    private lateinit var historyDataSource: HistoryDataSource
    private lateinit var historyDaoWithError: FakeHistoryDaoWithError
    private lateinit var historyDataSourceWithError: HistoryDataSource

    @Before
    fun setUp() {
        historyDao = FakeHistoryDao(originalHistoryDtos)
        historyDataSource = HistoryDataSourceImpl(historyDao, UnconfinedTestDispatcher())
        historyDaoWithError = FakeHistoryDaoWithError(originalHistoryDtos)
        historyDataSourceWithError =
            HistoryDataSourceImpl(historyDaoWithError, UnconfinedTestDispatcher())
    }

    @Test
    fun getItems() = runTest {
        val expected = historyDao.historyDtos
        var actual: List<HistoryDto>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            historyDataSource.getItems().collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getItemsWithError() = runTest {
        val expected = Result.failure<List<HistoryDto>>(ErrorEntity.RetryableError)
        var actual: Result<List<HistoryDto>>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            historyDaoWithError.getItems()
                .catch { actual = Result.failure(it) }
                .collect {}
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getPreviewItems() = runTest {
        val expected = historyDao.historyDtos.take(7)
        var actual: List<HistoryDto>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            historyDataSource.getPreviewItems().collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getPreviewItemsWithError() = runTest {
        val expected = Result.failure<List<HistoryDto>>(ErrorEntity.RetryableError)
        var actual: Result<List<HistoryDto>>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            historyDataSourceWithError.getPreviewItems()
                .catch { actual = Result.failure(it) }
                .collect {}
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun insertItem() = runTest {
        val expected = Result.success(Unit)
        val actual = historyDataSource.insertItem("I", "item8")
        assertEquals(expected, actual)
    }

    @Test
    fun insertItemWithError() = runTest {
        val expected = Result.failure<Unit>(ErrorEntity.UnknownError)
        val actual = historyDataSourceWithError.insertItem("I", "item8")
        assertEquals(expected, actual)
    }
}

class FakeHistoryDao(
    initHistoryDtos: List<HistoryDto> = listOf()
) : HistoryDao {
    val historyDtos = initHistoryDtos.toMutableList()

    override fun getItems(): Flow<List<HistoryDto>> {
        return flow { emit(historyDtos) }
    }

    override fun getPreviewItems(): Flow<List<HistoryDto>> {
        return flow { emit(historyDtos.apply { sortWith(compareBy { it.time }) }.take(7)) }
    }

    override suspend fun insertItem(item: HistoryDto) {
        return
    }
}

class FakeHistoryDaoWithError(
    initHistoryDtos: List<HistoryDto>
) : HistoryDao {
    override fun getItems(): Flow<List<HistoryDto>> {
        return flow { throw InterruptedByTimeoutException().toErrorEntity() }
    }

    override fun getPreviewItems(): Flow<List<HistoryDto>> {
        return flow { throw InterruptedIOException().toErrorEntity() }
    }

    override suspend fun insertItem(item: HistoryDto) {
        throw IllegalStateException()
    }
}