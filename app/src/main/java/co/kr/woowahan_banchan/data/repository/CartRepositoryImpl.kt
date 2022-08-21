package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.repository.CartRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDataSource: CartDataSource,
    private val detailDataSource: DetailDataSource,
    private val coroutineDispatcher: CoroutineDispatcher
) : CartRepository {
    override suspend fun addToCart(hash: String, amount: Int, name: String): Result<Unit> {
        val originalAmount = cartDataSource.getAmount(hash).getOrDefault(0)
        return cartDataSource.insertOrUpdateItems(
            listOf(CartDto(hash, amount + originalAmount, true, Date().time, name))
        )
    }

    override suspend fun updateCartItems(updateItems: List<CartItem>): Result<Unit> {
        return cartDataSource.insertOrUpdateItems(
            updateItems.map { CartDto(it.hash, it.amount, it.isSelected, Date().time, it.name) }
        )
    }

    override suspend fun deleteCartItems(ids: List<String>): Result<Unit> {
        return cartDataSource.deleteItems(ids)
    }

    override fun getCartItemCount(): Flow<Int> {
        return cartDataSource.getItems()
            .map { it.size }
    }

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartDataSource.getItems().map { cartItems ->
            cartItems.mapNotNull {
                val apiResult = detailDataSource.getDetail(it.hash)
                if (apiResult.isSuccess) {
                    apiResult.getOrThrow().data.toCartItem(
                        it.hash,
                        it.name,
                        it.isSelected,
                        it.amount
                    )
                } else {
                    null
                }
            }
        }.flowOn(coroutineDispatcher)
    }
}