package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.repository.CartRepository
import javax.inject.Inject

class UpdateCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(updateItems : List<CartItem>) : Result<Unit>{
        return runCatching {
            cartRepository.updateCartItems(updateItems)
        }
    }
}