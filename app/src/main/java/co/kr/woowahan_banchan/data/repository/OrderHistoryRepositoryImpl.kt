package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.order.OrderDataSource
import co.kr.woowahan_banchan.data.datasource.local.orderitem.OrderItemDataSource
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
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

    override fun getLatestOrderTime(): Flow<Long> =
        orderDataSource.getLatestOrderTime()

    override suspend fun getOrderTime(orderId: Long): Result<Long> {
        return orderDataSource.getTime(orderId)
    }

    override suspend fun getOrderReceipt(orderId: Long): Result<List<OrderItem>> {
        return withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                orderItemDataSource.getItems(orderId).getOrThrow().map {
                    it.toOrderItem()
                }
            }
        }
    }

    override suspend fun insertOrderItems(orderItems: List<CartItem>): Result<Long> {
        return runCatchingErrorEntity {
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
}