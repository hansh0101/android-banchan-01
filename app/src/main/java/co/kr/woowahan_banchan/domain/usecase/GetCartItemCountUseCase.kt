package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.CartRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCartItemCountUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(): Flow<Int> = cartRepository.getCartItemCount()
}