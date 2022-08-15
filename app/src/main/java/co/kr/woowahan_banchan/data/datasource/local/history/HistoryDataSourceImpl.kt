package co.kr.woowahan_banchan.data.datasource.local.history

import co.kr.woowahan_banchan.data.database.dao.HistoryDao
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class HistoryDataSourceImpl @Inject constructor(
    private val historyDao: HistoryDao,
    private val coroutineDispatcher: CoroutineDispatcher
) : HistoryDataSource {
    override fun getItems(): Flow<List<HistoryDto>> =
        historyDao.getItems()
            .catch { exception ->
                Timber.e(exception)
                emit(listOf())
            }.flowOn(coroutineDispatcher)

    override fun getPreviewItems(): Flow<List<HistoryDto>> =
        historyDao.getPreviewItems()
            .catch { exception ->
                Timber.e(exception)
                emit(listOf())
            }.flowOn(coroutineDispatcher)

    override suspend fun insertItem(hash: String, name: String) =
        withContext(coroutineDispatcher) {
            runCatching {
                historyDao.insertItem(
                    HistoryDto(hash, Date().time, name)
                )
            }
        }
}