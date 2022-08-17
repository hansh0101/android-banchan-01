package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.repository.CartRepository
import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository,
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(oldItems : List<CartItem>, cartItems : List<CartItem>) : Result<Long>{
        return runCatching {
            val orderItems = mutableListOf<CartItem>()
            val updateItem = mutableListOf<CartItem>()
            val deleteItem = mutableListOf<String>()
            oldItems.forEach { oldItem ->
                val temp = cartItems.find{it.hash == oldItem.hash}
                when {
                    temp == null -> deleteItem.add(oldItem.hash)
                    temp.isSelected -> orderItems.add(temp)
                    else -> updateItem.add(temp)
                }
            }
            val orderId = orderHistoryRepository.insertOrderItems(orderItems)
            cartRepository.deleteCartItems(orderItems.map { it.hash })

            cartRepository.updateCartItems(updateItem)
            cartRepository.deleteCartItems(deleteItem)

            orderId
        }
    }
}
