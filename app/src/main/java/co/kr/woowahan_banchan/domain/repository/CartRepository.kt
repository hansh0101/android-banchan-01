package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addToCart(hash: String, amount: Int, name: String): Result<Unit>
    suspend fun updateCartItems(updateItems: List<CartItem>): Result<Unit>
    suspend fun deleteCartItems(ids: List<String>): Result<Unit>
    fun getCartItemCount(): Flow<Result<Int>>
    fun getCartItems(): Flow<Result<List<CartItem>>>
}