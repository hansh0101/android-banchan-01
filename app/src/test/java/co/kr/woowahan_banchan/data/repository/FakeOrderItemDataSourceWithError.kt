package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.orderitem.OrderItemDataSource
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity

class FakeOrderItemDataSourceWithError: OrderItemDataSource {
    override suspend fun getItems(orderId: Long): Result<List<OrderItemDto>> {
        return Result.failure(ErrorEntity.ConditionalError)
    }

    override suspend fun getItem(orderId: Long): Result<OrderItemDto> {
        return Result.failure(ErrorEntity.UnknownError)
    }

    override suspend fun getItemCount(orderId: Long): Result<Int> {
        return Result.failure(ErrorEntity.RetryableError)
    }

    override suspend fun insertItems(items: List<OrderItemDto>): Result<Unit> {
        return Result.failure(ErrorEntity.ConditionalError)
    }
}