package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.order.OrderDataSource
import co.kr.woowahan_banchan.data.model.local.OrderDto
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeOrderDataSourceWithError: OrderDataSource {
    override suspend fun getItems(): Result<List<OrderDto>> {
        return Result.failure(ErrorEntity.RetryableError)
    }

    override fun getLatestOrderTime(): Flow<Result<Long>> {
        return flow { emit(Result.failure(ErrorEntity.ConditionalError)) }
    }

    override suspend fun getTime(orderId: Long): Result<Long> {
        return Result.failure(ErrorEntity.UnknownError)
    }

    override suspend fun insertItem(item: OrderDto): Result<Long> {
        return Result.failure(ErrorEntity.RetryableError)
    }
}