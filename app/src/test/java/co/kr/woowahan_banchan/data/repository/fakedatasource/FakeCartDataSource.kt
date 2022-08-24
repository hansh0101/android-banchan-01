package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartDataSource : CartDataSource {
    private val cartItems = mutableListOf(
        CartDto(
            hash = "HF778",
            amount = 1,
            isSelected = true,
            time = 111111,
            name = "소갈비찜"
        )
    )

    override fun getItems(): Flow<List<CartDto>> {
        return flow {
            emit(cartItems)
        }
    }

    override suspend fun insertOrUpdateItems(items: List<CartDto>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun deleteItems(ids: List<String>): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getAmount(hash: String): Result<Int> {
        val temp = cartItems.find { it.hash == hash }
        return if (temp != null) {
            Result.success(temp.amount)
        } else {
            Result.success(0)
        }
    }
}