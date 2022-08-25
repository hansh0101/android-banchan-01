package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class CheckOrderCompleteUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    operator fun invoke() = orderHistoryRepository.getOrderIsCompleted()
}