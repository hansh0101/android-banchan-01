package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(oldItems: List<CartItem>, cartItems: List<CartItem>): Result<Unit> {
        return runCatching {
            val updateItems = mutableListOf<CartItem>()
            val deleteItems = mutableListOf<String>()
            oldItems.forEach { oldItem ->
                val findResult = cartItems.find { it.hash == oldItem.hash }
                when {
                    findResult == null -> {
                        deleteItems.add(oldItem.hash)
                    }
                    oldItem.amount != findResult.amount || oldItem.isSelected != findResult.isSelected -> {
                        updateItems.add(findResult)
                    }
                }
            }
            cartRepository.updateCartItems(updateItems).getOrThrow()
            cartRepository.deleteCartItems(deleteItems).getOrThrow()
        }
    }
}