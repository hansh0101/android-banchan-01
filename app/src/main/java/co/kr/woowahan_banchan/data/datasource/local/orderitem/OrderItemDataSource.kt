package co.kr.woowahan_banchan.data.datasource.local.orderitem

import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import kotlinx.coroutines.flow.Flow

interface OrderItemDataSource {
    fun getItems(): Flow<List<OrderItemDto>>
    suspend fun insertItems(items: List<OrderItemDto>): Result<Unit>
}