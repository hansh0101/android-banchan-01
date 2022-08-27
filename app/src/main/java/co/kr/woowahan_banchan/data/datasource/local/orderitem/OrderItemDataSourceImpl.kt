package co.kr.woowahan_banchan.data.datasource.local.orderitem

import co.kr.woowahan_banchan.data.database.dao.OrderItemDao
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import co.kr.woowahan_banchan.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderItemDataSourceImpl @Inject constructor(
    private val orderItemDao: OrderItemDao,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : OrderItemDataSource {
    override suspend fun getItems(orderId: Long): Result<List<OrderItemDto>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderItemDao.getItems(orderId)
            }
        }

    override suspend fun getItem(orderId: Long): Result<OrderItemDto> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderItemDao.getItem(orderId)
            }
        }

    override suspend fun getItemCount(orderId: Long): Result<Int> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderItemDao.getItemCount(orderId)
            }
        }

    override suspend fun insertItems(items: List<OrderItemDto>): Result<Unit> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderItemDao.insertItems(items)
            }
        }
}