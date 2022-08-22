package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.repository.CartRepository
import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository,
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(oldItems: List<CartItem>, cartItems: List<CartItem>): Result<Long> {
        return runCatching {
            val orderItems = mutableListOf<CartItem>()
            val updateItems = mutableListOf<CartItem>()
            val deleteItems = mutableListOf<String>()
            oldItems.forEach { oldItem ->
                val findResult = cartItems.find { it.hash == oldItem.hash }
                when {
                    findResult == null -> {
                        deleteItems.add(oldItem.hash)
                    }
                    findResult.isSelected -> {
                        orderItems.add(findResult)
                    }
                    oldItem.amount != findResult.amount || oldItem.isSelected != findResult.isSelected -> {
                        updateItems.add(findResult)
                    }
                }
            }
            val orderId = orderHistoryRepository.insertOrderItems(orderItems).getOrThrow()
            cartRepository.deleteCartItems(orderItems.map { it.hash }).getOrThrow()
            cartRepository.updateCartItems(updateItems).getOrThrow()
            cartRepository.deleteCartItems(deleteItems).getOrThrow()

            orderId
        }
    }
}
