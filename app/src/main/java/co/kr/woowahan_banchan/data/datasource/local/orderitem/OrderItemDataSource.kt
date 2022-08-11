package co.kr.woowahan_banchan.data.datasource.local.orderitem

import co.kr.woowahan_banchan.data.model.local.OrderItemDto

interface OrderItemDataSource {
    suspend fun getItems(orderId: Long): Result<List<OrderItemDto>>
    suspend fun getItem(orderId: Long): Result<OrderItemDto>
    suspend fun getItemCount(orderId: Long): Result<Int>
    suspend fun insertItems(items: List<OrderItemDto>): Result<Unit>
}