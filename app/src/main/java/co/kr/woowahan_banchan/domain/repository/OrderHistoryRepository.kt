package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem

interface OrderHistoryRepository {
    suspend fun getOrderHistories(): List<OrderHistory>
    suspend fun getOrderTime(orderId: Long): Long
    suspend fun getOrderReceipt(orderId: Long): List<OrderItem>?
}