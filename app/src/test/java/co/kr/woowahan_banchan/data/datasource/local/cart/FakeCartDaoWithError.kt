package co.kr.woowahan_banchan.data.datasource.local.cart

import co.kr.woowahan_banchan.data.database.dao.CartDao
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow
import java.io.IOException
import java.io.InterruptedIOException

class FakeCartDaoWithError(
    initCartDtos: List<CartDto> = listOf()
) : CartDao {
    val cartDtos = initCartDtos.toMutableList()
    override fun getItems(): Flow<List<CartDto>> {
        TODO("Not yet implemented")
    }

    override suspend fun insertOrUpdateItems(items: List<CartDto>) {
        throw InterruptedIOException()
    }

    override suspend fun deleteItems(ids: List<String>) {
        throw IllegalStateException()
    }

    override suspend fun getAmount(hash: String): Int {
        throw IOException()
    }
}