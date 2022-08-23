package co.kr.woowahan_banchan.data.datasource.local.cart

import co.kr.woowahan_banchan.data.database.dao.CartDao
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CartDataSourceImpl @Inject constructor(
    private val cartDao: CartDao,
    private val coroutineDispatcher: CoroutineDispatcher
) : CartDataSource {
    override fun getItems(): Flow<List<CartDto>> =
        cartDao.getItems()
            .catch { exception ->
                throw exception.toErrorEntity()
            }.flowOn(coroutineDispatcher)

    override suspend fun insertOrUpdateItems(items: List<CartDto>): Result<Unit> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                cartDao.insertOrUpdateItems(items)
            }
        }

    override suspend fun deleteItems(ids: List<String>): Result<Unit> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                cartDao.deleteItems(ids)
            }
        }

    override suspend fun getAmount(hash: String): Result<Int> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                cartDao.getAmount(hash)
            }
        }
}