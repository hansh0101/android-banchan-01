package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(oldItems : List<CartItem>, cartItems : List<CartItem>) : Result<Unit>{
        return runCatching {
            val updateItem = mutableListOf<CartItem>()
            val deleteItem = mutableListOf<String>()
            oldItems.forEach { oldItem ->
                val temp = cartItems.find{it.hash == oldItem.hash}
                when {
                    temp == null -> deleteItem.add(oldItem.hash)
                    oldItem.amount != temp.amount ||
                    oldItem.isSelected != temp.isSelected -> updateItem.add(temp)
                }
            }
            cartRepository.updateCartItems(updateItem)
            cartRepository.deleteCartItems(deleteItem)
        }
    }
}