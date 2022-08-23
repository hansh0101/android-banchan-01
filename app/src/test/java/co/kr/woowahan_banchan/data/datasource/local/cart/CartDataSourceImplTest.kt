package co.kr.woowahan_banchan.data.datasource.local.cart

import co.kr.woowahan_banchan.data.database.dao.CartDao
import co.kr.woowahan_banchan.data.model.local.CartDto
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
import java.io.InterruptedIOException

@ExperimentalCoroutinesApi
class CartDataSourceImplTest {
    private val cartDto1 = CartDto("HBDEF", 1, true, 1661219851964, "오리 주물럭_반조리")
    private val cartDto2 = CartDto("HF778", 1, true, 1661219859605, "소갈비찜")
    private val updateCartDto = CartDto("HF778", 2, true, 1661219859605, "소갈비찜")
    private val newCartDto = CartDto("HEDFB", 1, true, 1661219864185, "쭈꾸미 한돈 제육볶음_반조림")
    private val originalCartDtos = listOf(cartDto1, cartDto2)

    private lateinit var cartDao: FakeCartDao
    private lateinit var cartDataSource: CartDataSource
    private lateinit var cartDaoWithError: FakeCartDaoWithError
    private lateinit var cartDataSourceWithError: CartDataSource

    @Before
    fun setUp() {
        cartDao = FakeCartDao(originalCartDtos)
        cartDataSource = CartDataSourceImpl(cartDao, UnconfinedTestDispatcher())
        cartDaoWithError = FakeCartDaoWithError(originalCartDtos)
        cartDataSourceWithError = CartDataSourceImpl(cartDaoWithError, UnconfinedTestDispatcher())
    }

    @Test
    fun getItems() = runTest {
        val expected = cartDao.cartDtos
        var actual: List<CartDto>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            cartDataSource.getItems().collect { actual = it }
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun getItemsWithError() = runTest {
        val expected = Result.failure<List<CartDto>>(ErrorEntity.RetryableError)
        var actual: Result<List<CartDto>>? = null
        val collectJob = launch(UnconfinedTestDispatcher()) {
            cartDataSourceWithError.getItems()
                .catch { actual = Result.failure(it) }
                .collect {}
        }
        assertEquals(expected, actual)
        collectJob.cancel()
    }

    @Test
    fun insertOrUpdateItems() = runTest {
        val expected = Result.success(Unit)
        val actual = cartDataSource.insertOrUpdateItems(listOf(updateCartDto, newCartDto))
        assertEquals(expected, actual)
    }

    @Test
    fun insertOrUpdateItemsWithError() = runTest {
        val expected = Result.failure<Unit>(ErrorEntity.RetryableError)
        val actual = cartDataSourceWithError.insertOrUpdateItems(listOf(updateCartDto, newCartDto))
        assertEquals(expected, actual)
    }

    @Test
    fun deleteItems() = runTest {
        val expected = Result.success(Unit)
        val actual = cartDataSource.deleteItems(listOf(cartDto2.hash))
        assertEquals(expected, actual)
    }

    @Test
    fun deleteItemsWithError() = runTest {
        val expected = Result.failure<Unit>(ErrorEntity.UnknownError)
        val actual = cartDataSourceWithError.deleteItems(listOf(cartDto2.hash))
        assertEquals(expected, actual)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getAmount() = runTest {
        val expected = Result.success(1)
        val actual = cartDataSource.getAmount(cartDto1.hash)
        assertEquals(expected, actual)
    }

    @Test
    fun getAmountWithError() = runTest {
        val expected = Result.failure<Int>(ErrorEntity.UnknownError)
        val actual = cartDataSourceWithError.getAmount(cartDto1.hash)
        assertEquals(expected, actual)
    }
}

class FakeCartDao(
    initCartDtos: List<CartDto> = listOf()
) : CartDao {
    val cartDtos = initCartDtos.toMutableList()

    override fun getItems(): Flow<List<CartDto>> {
        return flow { emit(cartDtos) }
    }

    override suspend fun insertOrUpdateItems(items: List<CartDto>) {
        return
    }

    override suspend fun deleteItems(ids: List<String>) {
        return
    }

    override suspend fun getAmount(hash: String): Int {
        return cartDtos.find { it.hash == hash }?.amount ?: 0
    }
}

class FakeCartDaoWithError(
    initCartDtos: List<CartDto> = listOf()
) : CartDao {
    val cartDtos = initCartDtos.toMutableList()

    override fun getItems(): Flow<List<CartDto>> {
        return flow { throw InterruptedIOException() }
    }

    override suspend fun insertOrUpdateItems(items: List<CartDto>) {
        throw InterruptedIOException()
    }

    override suspend fun deleteItems(ids: List<String>) {
        throw IllegalStateException()
    }

    override suspend fun getAmount(hash: String): Int {
        throw IOException()
    }
}