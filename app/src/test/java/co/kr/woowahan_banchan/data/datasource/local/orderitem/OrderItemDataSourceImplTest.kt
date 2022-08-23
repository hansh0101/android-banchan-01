package co.kr.woowahan_banchan.data.datasource.local.orderitem

import co.kr.woowahan_banchan.data.database.dao.OrderItemDao
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.net.UnknownHostException

@ExperimentalCoroutinesApi
class OrderItemDataSourceImplTest {
    private val orderItemDto1 = OrderItemDto(
        1,
        1,
        "HBBCC",
        "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_T.jpg",
        6000,
        2,
        "새콤달콤 오징어무침"
    )
    private val orderItemDto2 = OrderItemDto(
        2,
        2,
        "H1AA9",
        "http://public.codesquad.kr/jk/storeapp/data/main/739_ZIP_P__T.jpg",
        11800,
        3,
        "초계국수_쿠킹박스"
    )
    private val orderItemDto3 = OrderItemDto(
        3,
        3,
        "H26C7",
        "http://public.codesquad.kr/jk/storeapp/data/soup/818_ZIP_P_1033_T.jpg",
        11900,
        1,
        "순한 오징어무국"
    )
    private val originalOrderItemDtos = listOf(orderItemDto1, orderItemDto2)

    private lateinit var orderItemDao: FakeOrderItemDao
    private lateinit var orderItemDataSource: OrderItemDataSource
    private lateinit var orderItemDaoWithError: FakeOrderItemDaoWithError
    private lateinit var orderItemDataSourceWithError: OrderItemDataSource

    @Before
    fun setUp() {
        orderItemDao = FakeOrderItemDao(originalOrderItemDtos)
        orderItemDataSource = OrderItemDataSourceImpl(orderItemDao, UnconfinedTestDispatcher())
        orderItemDaoWithError = FakeOrderItemDaoWithError(originalOrderItemDtos)
        orderItemDataSourceWithError =
            OrderItemDataSourceImpl(orderItemDaoWithError, UnconfinedTestDispatcher())
    }

    @Test
    fun getItems() = runTest {
        val expected = Result.success(originalOrderItemDtos.filter { it.orderId == 1L })
        val actual = orderItemDataSource.getItems(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getItemsWithError() = runTest {
        val expected = Result.failure<List<OrderItemDto>>(ErrorEntity.UnknownError)
        val actual = orderItemDataSourceWithError.getItems(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getItem() = runTest {
        val expected = Result.success(orderItemDao.orderItemDtos.first { it.orderId == 1L })
        val actual = orderItemDataSource.getItem(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getItemWithError() = runTest {
        val expected = Result.failure<OrderItemDto>(ErrorEntity.ConditionalError)
        val actual = orderItemDataSourceWithError.getItem(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getItemCount() = runTest {
        val expected = Result.success(orderItemDao.orderItemDtos.filter { it.orderId == 1L }.size)
        val actual = orderItemDataSource.getItemCount(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getItemCountWithError() = runTest {
        val expected = Result.failure<Int>(ErrorEntity.UnknownError)
        val actual = orderItemDataSourceWithError.getItemCount(1)
        assertEquals(expected, actual)
    }

    @Test
    fun insertItems() = runTest {
        val expected = Result.success(Unit)
        val actual = orderItemDataSource.insertItems(listOf(orderItemDto3))
        assertEquals(expected, actual)
    }

    @Test
    fun insertItemsWithError() = runTest {
        val expected = Result.failure<Unit>(ErrorEntity.UnknownError)
        val actual = orderItemDataSourceWithError.insertItems(listOf(orderItemDto3))
        assertEquals(expected, actual)
    }
}

class FakeOrderItemDao(
    initOrderItemDtos: List<OrderItemDto>
) : OrderItemDao {
    val orderItemDtos = initOrderItemDtos.toMutableList()

    override suspend fun getItems(orderId: Long): List<OrderItemDto> {
        return orderItemDtos.filter { it.orderId == orderId }
    }

    override suspend fun getItem(orderId: Long): OrderItemDto {
        return orderItemDtos.first { it.orderId == orderId }
    }

    override suspend fun getItemCount(orderId: Long): Int {
        return orderItemDtos.filter { it.orderId == orderId }.size
    }

    override suspend fun insertItems(items: List<OrderItemDto>) {
        return
    }
}

class FakeOrderItemDaoWithError(
    initOrderItemDtos: List<OrderItemDto>
) : OrderItemDao {
    val orderItemDtos = initOrderItemDtos.toMutableList()

    override suspend fun getItems(orderId: Long): List<OrderItemDto> {
        throw IOException()
    }

    override suspend fun getItem(orderId: Long): OrderItemDto {
        throw UnknownHostException()
    }

    override suspend fun getItemCount(orderId: Long): Int {
        throw IllegalThreadStateException()
    }

    override suspend fun insertItems(items: List<OrderItemDto>) {
        throw ArrayIndexOutOfBoundsException()
    }
}