package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderListUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(): Result<List<OrderHistory>> =
        runCatching {
            orderHistoryRepository.getOrderHistories()
        }
}