package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import kotlinx.coroutines.flow.Flow

interface OrderHistoryRepository {
    suspend fun getOrderHistories(): Result<List<OrderHistory>>
    suspend fun getOrderTime(orderId: Long): Result<Long>
    suspend fun getOrderReceipt(orderId: Long): Result<List<OrderItem>>
    suspend fun insertOrderItems(orderItems: List<CartItem>): Result<Long>
    suspend fun updateOrderItems(orderHistory: OrderHistory): Result<Unit>
    fun getLatestOrderTime(): Flow<Result<Long>>
    fun getOrderIsCompleted(): Flow<Result<Boolean>>
}