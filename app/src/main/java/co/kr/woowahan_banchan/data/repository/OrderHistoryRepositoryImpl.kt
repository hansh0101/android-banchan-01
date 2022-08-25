package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.order.OrderDataSource
import co.kr.woowahan_banchan.data.datasource.local.orderitem.OrderItemDataSource
import co.kr.woowahan_banchan.data.model.local.OrderDto
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

class OrderHistoryRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource,
    private val orderItemDataSource: OrderItemDataSource,
    private val coroutineDispatcher: CoroutineDispatcher
) : OrderHistoryRepository {
    override suspend fun getOrderHistories(): Result<List<OrderHistory>> {
        return withContext(coroutineDispatcher) {
            orderDataSource.getItems()
                .mapCatching { orderList ->
                    orderList.mapNotNull {
                        val orderItem = orderItemDataSource.getItem(it.id).getOrNull()
                        val orderItemCount =
                            orderItemDataSource.getItemCount(it.id).getOrNull()
                        if (orderItem != null && orderItemCount != null) {
                            it.toOrderHistory(
                                orderItem.thumbnailUrl,
                                orderItem.name,
                                orderItemCount
                            )
                        } else {
                            null
                        }
                    }
                }
        }
    }

    override fun getLatestOrderTime(): Flow<Result<Long>> {
        return orderDataSource.getLatestOrderTime()
    }

    override suspend fun getOrderTime(orderId: Long): Result<Long> {
        return orderDataSource.getTime(orderId)
    }

    override suspend fun getOrderReceipt(orderId: Long): Result<List<OrderItem>> {
        return withContext(coroutineDispatcher) {
            orderItemDataSource.getItems(orderId)
                .mapCatching { orderItemsDto ->
                    orderItemsDto.map { it.toOrderItem() }
                }
        }
    }

    override suspend fun insertOrderItems(orderItems: List<CartItem>): Result<Long> {
        return runCatching {
            val orderId = orderDataSource.insertItem(
                OrderDto(totalPrice = orderItems.sumOf { it.price }, time = Date().time)
            ).getOrThrow()

            orderItemDataSource.insertItems(
                orderItems.map {
                    OrderItemDto(
                        orderId = orderId,
                        hash = it.hash,
                        thumbnailUrl = it.imageUrl,
                        price = it.price,
                        amount = it.amount,
                        name = it.name
                    )
                }
            ).getOrThrow()
            orderId
        }
    }

    override suspend fun updateOrderItems(orderHistory: OrderHistory): Result<Unit> {
        return orderDataSource.updateItem(
            OrderDto(
                id = orderHistory.orderId,
                totalPrice = orderHistory.totalPrice,
                time = orderHistory.time,
                isCompleted = orderHistory.isCompleted
            )
        )
    }

    override fun getIncompleteItemCount(): Flow<Result<Int>> {
        return orderDataSource.getIncompleteItemCount()
    }
}