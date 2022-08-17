package co.kr.woowahan_banchan.data.datasource.local.order

import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderDataSourceImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val coroutineDispatcher: CoroutineDispatcher
) : OrderDataSource {
    override suspend fun getItems(): Result<List<OrderDto>> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderDao.getItems()
            }
        }

    override fun getLatestOrderTime(): Flow<Long> =
        orderDao.getLatestOrderTime()
            .filterNotNull()
            .catch { exception ->
                Timber.e(exception)
                emit(0)
            }.flowOn(coroutineDispatcher)

    override suspend fun getTime(orderId: Long): Result<Long> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderDao.getTime(orderId)
            }
        }

    override suspend fun insertItem(item: OrderDto): Result<Long> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderDao.insertItem(item)
            }
        }
}