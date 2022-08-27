package co.kr.woowahan_banchan.data.datasource.local.history

import co.kr.woowahan_banchan.data.database.dao.HistoryDao
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import co.kr.woowahan_banchan.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class HistoryDataSourceImpl @Inject constructor(
    private val historyDao: HistoryDao,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : HistoryDataSource {
    override fun getItems(): Flow<Result<List<HistoryDto>>> =
        historyDao.getItems()
            .map { Result.success(it) }
            .catch { exception -> emit(Result.failure(exception.toErrorEntity())) }
            .flowOn(coroutineDispatcher)

    override fun getPreviewItems(): Flow<Result<List<HistoryDto>>> =
        historyDao.getPreviewItems()
            .map { Result.success(it) }
            .catch { exception -> emit(Result.failure(exception.toErrorEntity())) }
            .flowOn(coroutineDispatcher)

    override suspend fun insertItem(hash: String, name: String): Result<Unit> {
        return withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                historyDao.insertItem(
                    HistoryDto(hash, Date().time, name)
                )
            }
        }
    }
}