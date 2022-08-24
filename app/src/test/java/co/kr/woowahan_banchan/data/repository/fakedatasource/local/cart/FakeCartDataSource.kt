package co.kr.woowahan_banchan.data.repository.fakedatasource.local.cart

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartDataSource(
    private val cartDtos : MutableList<CartDto>
) : CartDataSource {

    override fun getItems(): Flow<Result<List<CartDto>>> {
        return flow {
            emit(Result.success(cartDtos))
        }
    }

    override suspend fun insertOrUpdateItems(items: List<CartDto>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteItems(ids: List<String>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getAmount(hash: String): Result<Int> {
        val temp = cartDtos.find { it.hash == hash }
        return if (temp != null) {
            Result.success(temp.amount)
        } else {
            Result.success(0)
        }
    }
}