package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.order.OrderDataSource
import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeOrderDataSource(initOrderDtos: List<OrderDto>) : OrderDataSource {
    val orderDtos = initOrderDtos.toMutableList()

    override suspend fun getItems(): Result<List<OrderDto>> {
        return Result.success(orderDtos)
    }

    override fun getLatestOrderTime(): Flow<Result<Long>> {
        return flow {
            emit(Result.success(orderDtos.apply { sortWith(compareByDescending { it.time }) }
                .first().time))
        }
    }

    override suspend fun getTime(orderId: Long): Result<Long> {
        return Result.success(orderDtos.find { it.id == orderId }!!.time)
    }

    override suspend fun insertItem(item: OrderDto): Result<Long> {
        return Result.success((orderDtos.apply { add(item) }.size).toLong())
    }
}