package co.kr.woowahan_banchan.data.datasource.local.order

import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.flow.Flow

interface OrderDataSource {
    suspend fun getItems(): Result<List<OrderDto>>
    suspend fun insertItem(item: OrderDto): Result<Long>
}