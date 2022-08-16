package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderHistoryRepository {
    suspend fun getOrderHistories(): List<OrderHistory>
    fun getLatestOrderTime(): Flow<Long>
    suspend fun getOrderTime(orderId: Long): Long
    suspend fun getOrderReceipt(orderId: Long): List<OrderItem>?
}