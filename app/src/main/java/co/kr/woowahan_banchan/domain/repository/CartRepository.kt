package co.kr.woowahan_banchan.domain.repository

interface CartRepository {
    suspend fun addToCart(hash: String, amount: Int, name: String)
}