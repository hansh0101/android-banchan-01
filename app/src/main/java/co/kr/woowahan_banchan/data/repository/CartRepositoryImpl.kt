package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class CartRepositoryImpl @Inject constructor(
    private val cartDataSource: CartDataSource
) : CartRepository {
    override suspend fun addToCart(hash: String, amount: Int, name: String) {
        val originalAmount = cartDataSource.getAmount(hash).getOrDefault(0)
        cartDataSource.insertOrUpdateItems(
            listOf(CartDto(hash, amount + originalAmount, true, Date().time, name))
        )
    }

    override fun getCartItemCount(): Flow<Int> {
        return cartDataSource.getItems()
            .map { it.size }
    }
}