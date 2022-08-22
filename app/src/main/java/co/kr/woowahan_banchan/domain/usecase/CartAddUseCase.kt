package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.CartRepository
import javax.inject.Inject

class CartAddUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(hash: String, amount: Int, name: String): Result<Unit> =
        cartRepository.addToCart(hash, amount, name)
}