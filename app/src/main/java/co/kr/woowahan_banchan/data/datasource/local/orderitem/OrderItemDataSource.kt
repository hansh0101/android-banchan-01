package co.kr.woowahan_banchan.data.datasource.local.orderitem

import co.kr.woowahan_banchan.data.model.local.OrderItemDto

interface OrderItemDataSource {
    suspend fun getItems(orderId: Int): Result<List<OrderItemDto>>
    suspend fun getItemCount(orderId: Int): Result<Int>
    suspend fun insertItems(items: List<OrderItemDto>): Result<Unit>
}