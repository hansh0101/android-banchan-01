package co.kr.woowahan_banchan.domain.repository

import kotlinx.coroutines.flow.Flow

interface CartRepository {
    suspend fun addToCart(hash: String, amount: Int, name: String)
    fun getCartItemCount(): Flow<Int>
}