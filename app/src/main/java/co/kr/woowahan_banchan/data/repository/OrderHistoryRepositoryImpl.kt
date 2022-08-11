package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.order.OrderDataSource
import co.kr.woowahan_banchan.data.datasource.local.orderitem.OrderItemDataSource
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import co.kr.woowahan_banchan.domain.repository.OrderHistoryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrderHistoryRepositoryImpl @Inject constructor(
    private val orderDataSource: OrderDataSource,
    private val orderItemDataSource: OrderItemDataSource,
    private val coroutineDispatcher: CoroutineDispatcher
) : OrderHistoryRepository {
    override suspend fun getOrderHistories(): List<OrderHistory> {
        return withContext(coroutineDispatcher) {
            val orderListResult = orderDataSource.getItems()
            when (orderListResult.isSuccess) {
                true -> {
                    orderListResult.getOrDefault(listOf()).mapNotNull {
                        val orderItemList = orderItemDataSource.getItems(it.id).getOrNull()
                        val orderItemCount = orderItemDataSource.getItemCount(it.id).getOrNull()
                        if (orderItemList == null || orderItemCount == null) {
                            null
                        } else {
                            it.toOrderHistory(
                                orderItemList.first().thumbnailUrl,
                                orderItemList.first().name,
                                orderItemCount
                            )
                        }
                    }
                }
                false -> {
                    listOf()
                }
            }
        }
    }

    override suspend fun getOrderReceipt(orderId: Long): List<OrderItem>? {
        return withContext(coroutineDispatcher) {
            val orderItemsResult = orderItemDataSource.getItems(orderId)
            when (orderItemsResult.isSuccess) {
                true -> {
                    orderItemsResult.getOrDefault(listOf()).map {
                        it.toOrderItem()
                    }
                }
                false -> {
                    null
                }
            }
        }
    }
}