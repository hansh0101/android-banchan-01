package co.kr.woowahan_banchan.data.datasource.local.order

import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderDataSourceImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val coroutineDispatcher: CoroutineDispatcher
) : OrderDataSource {
    override suspend fun getItems(): Result<List<OrderDto>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderDao.getItems()
            }
        }

    override fun getLatestOrderTime(): Flow<Long> =
        orderDao.getLatestOrderTime()
            .filterNotNull()
            .catch { exception ->
                throw exception.toErrorEntity()
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
}