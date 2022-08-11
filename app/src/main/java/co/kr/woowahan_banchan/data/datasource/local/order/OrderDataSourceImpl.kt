package co.kr.woowahan_banchan.data.datasource.local.order

import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class OrderDataSourceImpl @Inject constructor(
    private val orderDao: OrderDao,
    private val coroutineDispatcher: CoroutineDispatcher
) : OrderDataSource {
    override fun getItems(): Flow<List<OrderDto>> =
        orderDao.getItems()
            .catch { exception ->
                Timber.e(exception)
                emit(listOf())
            }.flowOn(coroutineDispatcher)

    override suspend fun insertItem(item: OrderDto): Result<Long> =
        withContext(coroutineDispatcher) {
            runCatching {
                orderDao.insertItem(item)
            }
        }
}