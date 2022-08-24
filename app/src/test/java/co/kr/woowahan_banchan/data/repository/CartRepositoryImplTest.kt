package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.data.model.remote.response.DetailDataResponse
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
import co.kr.woowahan_banchan.data.repository.fakedatasource.local.cart.FakeCartDataSource
import co.kr.woowahan_banchan.data.repository.fakedatasource.local.cart.FakeCartDataSourceWithError
import co.kr.woowahan_banchan.data.repository.fakedatasource.remote.detail.FakeDetailDataSource
import co.kr.woowahan_banchan.data.repository.fakedatasource.remote.detail.FakeDetailDataSourceWithError
import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.repository.CartRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class CartRepositoryImplTest {


    private val cartDto = CartDto("HF778", 1, true, 111111, "소갈비찜")
    private val cartItem = CartItem("HF778", "소갈비찜", true, 1, "", 26010)

    private val cartDtos = listOf(cartDto)

    private val detailResponse = DetailResponse(
        hash = "HF778",
        data = DetailDataResponse(
            topImageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
            thumbnailUrls = listOf(
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_S.jpg"
            ),
            productDescription = "촉촉하게 밴 양념이 일품",
            point = "260원",
            deliveryInfo = "서울 경기 새벽 배송 / 전국 택배 배송",
            deliveryFee = "2,500원 (40,000원 이상 구매 시 무료)",
            prices = listOf("28,900원", "26,010원"),
            detailSection = listOf(
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D1.jpg",
                "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D2.jpg",
                "http://public.codesquad.kr/jk/storeapp/data/pakage_regular.jpg"
            )
        )
    )

    private lateinit var cartDataSource: CartDataSource
    private lateinit var cartDataSourceWithError: CartDataSource

    private lateinit var detailDataSource: DetailDataSource
    private lateinit var detailDataSourceWithError: DetailDataSource

    private lateinit var cartRepository: CartRepository
    private lateinit var cartRepositoryWithError: CartRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        cartDataSource = FakeCartDataSource(cartDtos.toMutableList())
        cartDataSourceWithError = FakeCartDataSourceWithError()
        detailDataSource = FakeDetailDataSource(detailResponse)
        detailDataSourceWithError = FakeDetailDataSourceWithError()
        cartRepository = CartRepositoryImpl(cartDataSource, detailDataSource, testDispatcher)
        cartRepositoryWithError =
            CartRepositoryImpl(cartDataSourceWithError, detailDataSourceWithError, testDispatcher)
    }

    @Test
    fun addToCartTest() {
        runTest {
            val expected = Result.success(Unit)
            val actual = cartRepository.addToCart("HF778", 1, "소갈비찜")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun updateCartItemsTest() {
        runTest {
            val expected = Result.success(Unit)
            val actual = cartRepository.updateCartItems(
                listOf(
                    cartItem
                )
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun deleteCartItemsTest() {
        runTest {
            val expected = Result.success(Unit)
            val actual = cartRepository.deleteCartItems(listOf(cartItem.hash))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun getCartItemCountTest() {
        runTest {
            val expected = Result.success(1)
            var actual: Result<Int>? = null
            val collectJob = launch(UnconfinedTestDispatcher()) {
                cartRepository.getCartItemCount().collect {
                    actual = it
                }
            }
            assertEquals(expected, actual)
            collectJob.cancel()
        }
    }

    @Test
    fun getCartItemsTest() {
        runTest {
            val expected = Result.success(
                listOf(
                    CartItem(
                        hash = "HF778",
                        name = "소갈비찜",
                        isSelected = true,
                        amount = 1,
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                        price = 26010
                    )
                )
            )
            var actual: Result<List<CartItem>>? = null
            cartRepository.getCartItems().collect {
                actual = it
            }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun addToCartErrorTest() {
        runTest {
            val expected = Result.failure<Unit>(ErrorEntity.UnknownError)
            val actual = cartRepositoryWithError.addToCart("HF778", 1, "소갈비찜")
            assertEquals(expected, actual)
        }
    }

    @Test
    fun updateCartItemsErrorTest() {
        runTest {
            val expected = Result.failure<Unit>(ErrorEntity.UnknownError)
            val actual = cartRepositoryWithError.updateCartItems(
                listOf(
                    cartItem
                )
            )
            assertEquals(expected, actual)
        }
    }

    @Test
    fun deleteCartItemsErrorTest() {
        runTest {
            val expected = Result.failure<Unit>(ErrorEntity.UnknownError)
            val actual = cartRepositoryWithError.deleteCartItems(listOf(cartItem.hash))
            assertEquals(expected, actual)
        }
    }

    @Test
    fun getCartItemCountErrorTest() {
        runTest {
            val expected = Result.failure<Int>(ErrorEntity.UnknownError)
            var actual: Result<Int>? = null
            val collectJob = launch(testDispatcher) {
                cartRepositoryWithError.getCartItemCount().collect {
                    actual = it
                }
            }
            assertEquals(expected, actual)
            collectJob.cancel()
        }
    }

    @Test
    fun getCartItemsErrorTest() {
        runTest {
            val expected = Result.failure<List<CartItem>>(ErrorEntity.UnknownError)
            var actual: Result<List<CartItem>>? = null
            val collectJob = launch(testDispatcher) {
                cartRepositoryWithError.getCartItems().collect {
                    actual = it
                }
            }
            assertEquals(expected, actual)
            collectJob.cancel()
        }
    }
}