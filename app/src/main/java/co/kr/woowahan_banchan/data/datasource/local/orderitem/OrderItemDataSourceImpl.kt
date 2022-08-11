package co.kr.woowahan_banchan.data.datasource.local.orderitem

import co.kr.woowahan_banchan.data.database.dao.OrderItemDao
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderItemDataSourceImpl @Inject constructor(
    private val orderItemDao: OrderItemDao,
    private val coroutineDispatcher: CoroutineDispatcher
): OrderItemDataSource {
    override suspend fun getItems(orderId: Long): Result<List<OrderItemDto>> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderItemDao.getItems(orderId)
            }
        }

    override suspend fun getItemCount(orderId: Long): Result<Int> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderItemDao.getItemCount(orderId)
            }
        }

    override suspend fun insertItems(items: List<OrderItemDto>): Result<Unit> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderItemDao.insertItems(items)
            }
        }
}