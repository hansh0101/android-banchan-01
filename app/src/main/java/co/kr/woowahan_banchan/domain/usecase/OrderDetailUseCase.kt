package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import javax.inject.Inject

class OrderDetailUseCase @Inject constructor(
    private val orderHistoryRepository: OrderHistoryRepository
) {
    suspend operator fun invoke(orderId: Long): Result<List<OrderItem>> =
        orderHistoryRepository.getOrderReceipt(orderId)
}