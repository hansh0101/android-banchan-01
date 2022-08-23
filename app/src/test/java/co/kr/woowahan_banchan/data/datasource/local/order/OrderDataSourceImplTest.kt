package co.kr.woowahan_banchan.data.datasource.local.order

import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.model.local.OrderDto
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class OrderDataSourceImplTest {
    private val orderDto1 = OrderDto(1, 6000, 1661073342694)
    private val orderDto2 = OrderDto(2, 11800, 1661073350797)
    private val orderDto3 = OrderDto(3, 28110, 1661073395292)
    private val originalOrderDtos = listOf(orderDto1, orderDto2)

    private lateinit var orderDao: FakeOrderDao
    private lateinit var orderDataSource: OrderDataSource
    private lateinit var orderDaoWithError: FakeOrderDaoWithError
    private lateinit var orderDataSourceWithError: OrderDataSource

    @Before
    fun setUp() {
        orderDao = FakeOrderDao(originalOrderDtos)
        orderDataSource = OrderDataSourceImpl(orderDao, UnconfinedTestDispatcher())
        orderDaoWithError = FakeOrderDaoWithError(originalOrderDtos)
        orderDataSourceWithError =
            OrderDataSourceImpl(orderDaoWithError, UnconfinedTestDispatcher())
    }

    @Test
    fun getItems() = runTest {
        val expected = Result.success(orderDao.orderDtos)
        val actual = orderDataSource.getItems()
        assertEquals(expected, actual)
    }

    @Test
    fun getItemsWithError() = runTest {
        val expected = Result.failure<List<OrderDto>>(ErrorEntity.UnknownError)
        val actual = orderDataSourceWithError.getItems()
        assertEquals(expected, actual)
    }

    @Test
    fun getLatestOrderTime() = runTest {
        val expected =
            orderDao.orderDtos.apply { sortWith(compareByDescending { it.time }) }.first().time
        var actual: Long? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            orderDataSource.getLatestOrderTime().collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getLatestOrderTimeWithError() = runTest {
        val expected = Result.failure<Long>(ErrorEntity.UnknownError)
        var actual: Result<Long>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            orderDataSourceWithError.getLatestOrderTime()
                .catch { actual = Result.failure(it) }
                .collect {}
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getTime() = runTest {
        val expected = Result.success(requireNotNull(orderDao.orderDtos.find { it.id == 2L }).time)
        val actual = orderDataSource.getTime(2L)
        assertEquals(expected, actual)
    }

    @Test
    fun getTimeWithError() = runTest {
        val expected = Result.failure<Long>(ErrorEntity.UnknownError)
        val actual = orderDataSourceWithError.getTime(1)
        assertEquals(expected, actual)
    }

    @Test
    fun insertItem() = runTest {
        val expected = Result.success(3L)
        val actual = orderDataSource.insertItem(orderDto3)
        assertEquals(expected, actual)
    }

    @Test
    fun insertItemWithError() = runTest {
        val expected = Result.failure<Long>(ErrorEntity.UnknownError)
        val actual = orderDataSourceWithError.insertItem(orderDto3)
        assertEquals(expected, actual)
    }
}

class FakeOrderDao(initOrderDtos: List<OrderDto>) : OrderDao {
    val orderDtos = initOrderDtos.toMutableList()

    override suspend fun getItems(): List<OrderDto> {
        return orderDtos
    }

    override fun getLatestOrderTime(): Flow<Long> {
        return flow {
            emit(orderDtos.apply { sortWith(compareByDescending { it.time }) }.first().time)
        }
    }

    override suspend fun getTime(orderId: Long): Long {
        return requireNotNull(orderDtos.find { it.id == orderId }).time
    }

    override suspend fun insertItem(item: OrderDto): Long {
        return (orderDtos.size + 1).toLong()
    }
}

class FakeOrderDaoWithError(initOrderDtos: List<OrderDto>) : OrderDao {
    val orderDtos = initOrderDtos.toMutableList()

    override suspend fun getItems(): List<OrderDto> {
        throw IllegalStateException()
    }

    override fun getLatestOrderTime(): Flow<Long> {
        return flow { throw ArithmeticException() }
    }

    override suspend fun getTime(orderId: Long): Long {
        throw IOException()
    }

    override suspend fun insertItem(item: OrderDto): Long {
        throw NullPointerException()
    }
}