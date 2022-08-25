package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CheckOrderCompleteUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    operator fun invoke() : Flow<Result<Boolean>> = orderHistoryRepository.getOrderIsCompleted()
}