package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.CartRepository
import javax.inject.Inject

class DeleteCartItemsUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    suspend operator fun invoke(deleteIds: List<String>): Result<Unit> {
        return runCatching { cartRepository.deleteCartItems(deleteIds) }
    }
}