package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.local.orderitem.OrderItemDataSource
import co.kr.woowahan_banchan.data.model.local.OrderItemDto

class FakeOrderItemDataSource(initOrderItemDtos: List<OrderItemDto>) : OrderItemDataSource {
    val orderItemDtos = initOrderItemDtos.toMutableList()

    override suspend fun getItems(orderId: Long): Result<List<OrderItemDto>> {
        return Result.success(orderItemDtos.filter { it.orderId == orderId })
    }

    override suspend fun getItem(orderId: Long): Result<OrderItemDto> {
        return Result.success(orderItemDtos.first { it.orderId == orderId })
    }

    override suspend fun getItemCount(orderId: Long): Result<Int> {
        return Result.success(orderItemDtos.filter { it.orderId == orderId }.size)
    }

    override suspend fun insertItems(items: List<OrderItemDto>): Result<Unit> {
        return Result.success(Unit)
    }
}