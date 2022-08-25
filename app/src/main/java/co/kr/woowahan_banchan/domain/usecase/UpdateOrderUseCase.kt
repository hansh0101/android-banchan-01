package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class UpdateOrderUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(orderId: Long):Result<Unit> =
        orderHistoryRepository.updateOrderHistory(orderId)
}