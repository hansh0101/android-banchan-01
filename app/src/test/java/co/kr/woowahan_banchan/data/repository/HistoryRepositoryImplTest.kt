package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import co.kr.woowahan_banchan.data.model.remote.response.DetailDataResponse
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
import co.kr.woowahan_banchan.data.repository.fakedatasource.local.cart.FakeCartDataSource
import co.kr.woowahan_banchan.data.repository.fakedatasource.local.cart.FakeCartDataSourceWithError
import co.kr.woowahan_banchan.data.repository.fakedatasource.local.history.FakeHistoryDataSource
import co.kr.woowahan_banchan.data.repository.fakedatasource.local.history.FakeHistoryDataSourceWithError
import co.kr.woowahan_banchan.data.repository.fakedatasource.remote.detail.FakeDetailDataSource
import co.kr.woowahan_banchan.data.repository.fakedatasource.remote.detail.FakeDetailDataSourceWithError
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HistoryRepositoryImplTest {
    private val historyDto1 = HistoryDto("HF778", 1661068772834, "소갈비찜")

    //    private val historyDto2 = HistoryDto("H1AA9", 1661073362608, "초계국수_쿠킹박스")
    private val cartDto1 = CartDto("HF778", 2, true, 1661172787438, "소갈비찜")

    //    private val cartDto2 = CartDto("H1AA9", 1, true, 1661245549658, "초계국수_쿠킹박스")
    private val detailResponse1 = DetailResponse(
        hash = "HF778",
        data = DetailDataResponse(
            topImageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
            thumbnailUrls = listOf(
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_S.jpg"
            ),
            productDescription = "촉촉하게 밴 양념이 일품",
            point = "260원",
            deliveryInfo = "서울 경기 새벽 배송 / 전국 택배 배송",
            deliveryFee = "2,500원 (40,000원 이상 구매 시 무료)",
            prices = listOf("28,900원", "26,010원"),
            detailSection = listOf(
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D1.jpg",
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D2.jpg",
                "http://public.codesquad.kr/jk/storeapp/data/pakage_regular.jpg"
            )
        )
    )

    private lateinit var historyDataSource: FakeHistoryDataSource
    private lateinit var cartDataSource: FakeCartDataSource
    private lateinit var detailDataSource: FakeDetailDataSource
    private lateinit var historyRepository: HistoryRepositoryImpl
    private lateinit var historyDataSourceWithError: FakeHistoryDataSourceWithError
    private lateinit var cartDataSourceWithError: FakeCartDataSourceWithError
    private lateinit var detailDataSourceWithError: FakeDetailDataSourceWithError
    private lateinit var historyRepositoryWithError: HistoryRepositoryImpl
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        historyDataSource = FakeHistoryDataSource(listOf(historyDto1))
        cartDataSource = FakeCartDataSource(mutableListOf(cartDto1))
        detailDataSource = FakeDetailDataSource(detailResponse1)
        historyRepository = HistoryRepositoryImpl(
            historyDataSource,
            cartDataSource,
            detailDataSource,
            testDispatcher
        )
        historyDataSourceWithError = FakeHistoryDataSourceWithError()
        cartDataSourceWithError = FakeCartDataSourceWithError()
        detailDataSourceWithError = FakeDetailDataSourceWithError()
        historyRepositoryWithError = HistoryRepositoryImpl(
            historyDataSourceWithError,
            cartDataSourceWithError,
            detailDataSourceWithError,
            testDispatcher
        )
    }

    @Test
    fun addToHistory() = runTest {
        val expected = Result.success(Unit)
        val actual = historyRepository.addToHistory("HASH1", "NAME_OF_PRODUCT")
        assertEquals(expected, actual)
    }

    @Test
    fun addToHistoryWithError() = runTest {
        val expected = Result.failure<Unit>(ErrorEntity.UnknownError)
        val actual = historyRepositoryWithError.addToHistory("HASH1", "NAME_OF_PRODUCT")
        assertEquals(expected, actual)
    }

    @Test
    fun getHistories() = runTest(testDispatcher) {
        val expected = Result.success(
            listOf(
                HistoryItem(
                    detailHash = "HF778",
                    imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                    title = "소갈비찜",
                    nPrice = 28900,
                    sPrice = 26010,
                    time = 1661068772834,
                    isAdded = true
                )
            )
        )
        var actual: Result<List<HistoryItem>>? = null
        val collectJob = launch(testDispatcher) {
            historyRepository.getHistories(true).collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getHistoriesWithError() = runTest(testDispatcher) {
        val expected = Result.failure<List<HistoryItem>>(ErrorEntity.RetryableError)
        var actual: Result<List<HistoryItem>>? = null
        val collectJob = launch(testDispatcher) {
            historyRepositoryWithError.getHistories(false).collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getHistoriesPreviewWithError() = runTest(testDispatcher) {
        val expected = Result.failure<List<HistoryItem>>(ErrorEntity.UnknownError)
        var actual: Result<List<HistoryItem>>? = null
        val collectJob = launch(testDispatcher) {
            historyRepositoryWithError.getHistories(true).collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }
}