package co.kr.woowahan_banchan.data.datasource.local.order

import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.flow.Flow

interface OrderDataSource {
    suspend fun getItems(): Result<List<OrderDto>>
    suspend fun getTime(orderId: Long): Result<Long>
    suspend fun insertItem(item: OrderDto): Result<Long>
    suspend fun updateItem(item: OrderDto): Result<Unit>
    fun getLatestOrderTime(): Flow<Result<Long>>
    fun getIncompleteItemCount(): Flow<Result<Int>>
}