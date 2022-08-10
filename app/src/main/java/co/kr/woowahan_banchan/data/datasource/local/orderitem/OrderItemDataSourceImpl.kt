package co.kr.woowahan_banchan.data.datasource.local.orderitem

import co.kr.woowahan_banchan.data.database.dao.OrderItemDao
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderItemDataSourceImpl @Inject constructor(
    private val orderItemDao: OrderItemDao,
    private val coroutineDispatcher: CoroutineDispatcher
): OrderItemDataSource {
    override fun getItems(): Flow<List<OrderItemDto>> =
        orderItemDao.getItems()
            .catch { exception ->
                Timber.e(exception)
                emit(listOf())
            }.flowOn(coroutineDispatcher)

    override suspend fun insertItems(items: List<OrderItemDto>): Result<Unit> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderItemDao.insertItems(items)
            }
        }
}