package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.model.local.OrderDto
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class OrderHistoryRepositoryImplTest {
    private val orderDto1 = OrderDto(1, 12640, 1661211592525)
    private val orderDto2 = OrderDto(2, 42910, 1661211606725)
    private val orderDto3 = OrderDto(3, 23700, 1661212098509)
    private val orderItemDto1 = OrderItemDto(
        1,
        1,
        "HBDEF",
        "http://public.codesquad.kr/jk/storeapp/data/main/1155_ZIP_P_0081_T.jpg",
        12640,
        1,
        "오리 주물럭_반조리"
    )
    private val orderItemDto2 = OrderItemDto(
        2,
        2,
        "HEDFB",
        "http://public.codesquad.kr/jk/storeapp/data/main/510_ZIP_P_0047_T.jpg",
        16900,
        1,
        "쭈꾸미 한돈 제육볶음_반조림"
    )
    private val orderItemDto3 = OrderItemDto(
        3,
        2,
        "HF778",
        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
        26010,
        2,
        "소갈비찜"
    )
    private val orderItemDto4 = OrderItemDto(
        4,
        3,
        "H1AA9",
        "http://public.codesquad.kr/jk/storeapp/data/main/739_ZIP_P__T.jpg",
        11800,
        2,
        "초계국수_쿠킹박스"
    )
    private val orderItemDto5 = OrderItemDto(
        5,
        3,
        "H26C7",
        "http://public.codesquad.kr/jk/storeapp/data/soup/818_ZIP_P_1033_T.jpg",
        11900,
        1,
        "순한 오징어무국"
    )

    private lateinit var orderDataSource: FakeOrderDataSource
    private lateinit var orderItemDataSource: FakeOrderItemDataSource
    private lateinit var orderHistoryRepository: OrderHistoryRepositoryImpl
    private lateinit var orderDataSourceWithError: FakeOrderDataSourceWithError
    private lateinit var orderItemDataSourceWithError: FakeOrderItemDataSourceWithError
    private lateinit var orderHistoryRepositoryWithError: OrderHistoryRepositoryImpl

    @Before
    fun setUp() {
        orderDataSource =
            FakeOrderDataSource(listOf(orderDto1, orderDto2))
        orderItemDataSource =
            FakeOrderItemDataSource(listOf(orderItemDto1, orderItemDto2, orderItemDto3))
        orderHistoryRepository = OrderHistoryRepositoryImpl(
            orderDataSource,
            orderItemDataSource,
            UnconfinedTestDispatcher()
        )
        orderDataSourceWithError = FakeOrderDataSourceWithError()
        orderItemDataSourceWithError = FakeOrderItemDataSourceWithError()
        orderHistoryRepositoryWithError = OrderHistoryRepositoryImpl(
            orderDataSourceWithError,
            orderItemDataSourceWithError,
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun getOrderHistories() = runTest {
        val expected = Result.success(
            listOf(
                orderDto1.toOrderHistory(orderItemDto1.thumbnailUrl, orderItemDto1.name, 1),
                orderDto2.toOrderHistory(orderItemDto2.thumbnailUrl, orderItemDto2.name, 2)
            )
        )
        val actual = orderHistoryRepository.getOrderHistories()
        assertEquals(expected, actual)
    }

    @Test
    fun getOrderHistoriesWithError() = runTest {
        val expected = Result.failure<List<OrderHistory>>(ErrorEntity.RetryableError)
        val actual = orderHistoryRepositoryWithError.getOrderHistories()
        assertEquals(expected, actual)
    }

    @Test
    fun getLatestOrderTime() = runTest {
        val expected =
            Result.success(orderDataSource.orderDtos.apply { sortWith(compareByDescending { it.time }) }
                .first().time)
        var actual: Result<Long>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            orderHistoryRepository.getLatestOrderTime().collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getLatestOrderTimeWithError() = runTest {
        val expected = Result.failure<Long>(ErrorEntity.ConditionalError)
        var actual: Result<Long>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            orderHistoryRepositoryWithError.getLatestOrderTime().collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getOrderTime() = runTest {
        val expected = Result.success(orderDto1.time)
        val actual = orderHistoryRepository.getOrderTime(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getOrderTimeWithError() = runTest {
        val expected = Result.failure<Long>(ErrorEntity.UnknownError)
        val actual = orderHistoryRepositoryWithError.getOrderTime(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getOrderReceipt() = runTest {
        val expected = Result.success(listOf(orderItemDto1).map { it.toOrderItem() })
        val actual = orderHistoryRepository.getOrderReceipt(1)
        assertEquals(expected, actual)
    }

    @Test
    fun getOrderReceiptWithError() = runTest {
        val expected = Result.failure<List<OrderItem>>(ErrorEntity.ConditionalError)
        val actual = orderHistoryRepositoryWithError.getOrderReceipt(1)
        assertEquals(expected, actual)
    }

    @Test
    fun insertOrderItems() = runTest {
        val expected = Result.success((orderDataSource.orderDtos.size + 1).toLong())
        val actual = orderHistoryRepository.insertOrderItems(listOf())
        assertEquals(expected, actual)
    }

    @Test
    fun insertOrderItemsWithError() = runTest {
        val expected = Result.failure<Long>(ErrorEntity.RetryableError)
        val actual = orderHistoryRepositoryWithError.insertOrderItems(listOf())
        assertEquals(expected, actual)
    }
}