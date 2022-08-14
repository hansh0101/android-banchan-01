package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderTimeUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(orderId: Long): Result<Long> =
        runCatching {
            orderHistoryRepository.getOrderTime(orderId)
        }
}