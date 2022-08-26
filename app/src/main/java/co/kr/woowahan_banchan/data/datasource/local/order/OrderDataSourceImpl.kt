package co.kr.woowahan_banchan.data.datasource.local.order

import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.OrderDto
import co.kr.woowahan_banchan.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderDataSourceImpl @Inject constructor(
    private val orderDao: OrderDao,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : OrderDataSource {
    override suspend fun getItems(): Result<List<OrderDto>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderDao.getItems()
            }
        }

    override fun getLatestOrderTime(): Flow<Result<Long>> =
        orderDao.getLatestOrderTime()
            .filterNotNull()
            .map { Result.success(it) }
            .catch { exception ->
                emit(Result.failure(exception.toErrorEntity()))
            }.flowOn(coroutineDispatcher)

    override suspend fun getTime(orderId: Long): Result<Long> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderDao.getTime(orderId)
            }
        }

    override suspend fun insertItem(item: OrderDto): Result<Long> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderDao.insertItem(item)
            }
        }

    override fun getOrderIsCompleted(): Flow<Result<Boolean>> =
        orderDao.getIncompleteItems()
            .filterNotNull()
            .map { Result.success(it.isEmpty()) }
            .catch {
                emit(Result.failure(it.toErrorEntity()))
            }.flowOn(coroutineDispatcher)

    override suspend fun updateItem(orderId : Long): Result<Unit> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderDao.updateItem(orderId)
            }
        }
}