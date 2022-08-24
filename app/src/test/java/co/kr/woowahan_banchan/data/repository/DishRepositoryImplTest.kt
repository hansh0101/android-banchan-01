package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSource
import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import co.kr.woowahan_banchan.data.repository.fakedatasource.*
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.repository.DishRepository
import co.kr.woowahan_banchan.domain.repository.Source
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DishRepositoryImplTest {

    private val mainDish1 = DishResponse(
        detailHash = "HBDEF",
        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/1155_ZIP_P_0081_T.jpg",
        alt = "오리 주물럭_반조리",
        deliveryType = listOf("새벽배송", "전국택배"),
        title = "오리 주물럭_반조리",
        description = "감질맛 나는 매콤한 양념",
        nPrice = "15,800원",
        sPrice = "12,640원",
        badge = listOf("런칭특가")
    )
    private val mainDish2 = DishResponse(
        detailHash = "HF778",
        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
        alt = "소갈비찜",
        deliveryType = listOf("새벽배송", "전국택배"),
        title = "소갈비찜",
        description = "촉촉하게 밴 양념이 일품",
        nPrice = "28,900원",
        sPrice = "26,010원",
        badge = listOf("이벤트특가", "메인특가")
    )
    private val soupDish1 = DishResponse(
        detailHash = "H72C3",
        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/soup/28_ZIP_P_1003_T.jpg",
        alt = "한돈 돼지 김치찌개",
        deliveryType = listOf("새벽배송", "전국택배"),
        title = "한돈 돼지 김치찌개",
        description = "김치찌개에는 역시 돼지고기",
        nPrice = "9,300원",
        sPrice = "8,370원",
        badge = listOf("이벤트특가")
    )
    private val soupDish2 = DishResponse(
        detailHash = "HA6EE",
        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/soup/33_ZIP_P_1004_T.jpg",
        alt = "된장찌개",
        deliveryType = listOf("새벽배송", "전국택배"),
        title = "된장찌개",
        description = "특별하지 않아서 더 좋은 우리맛",
        nPrice = "8,800원",
        sPrice = "7,920원",
        badge = listOf("이벤트특가")
    )
    private val sideDish1 = DishResponse(
        detailHash = "HBBCC",
        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_T.jpg",
        alt = "새콤달콤 오징어무침",
        deliveryType = listOf("새벽배송", "전국택배"),
        title = "새콤달콤 오징어무침",
        description = "국내산 오징어를 새콤달콤하게",
        nPrice = "7,500원",
        sPrice = "6,000원",
        badge = listOf("런칭특가")
    )
    private val sideDish2 = DishResponse(
        detailHash = "H1939",
        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/17_ZIP_P_6014_T.jpg",
        alt = "호두 멸치볶음",
        deliveryType = listOf("새벽배송", "전국택배"),
        title = "호두 멸치볶음",
        description = "잔멸치와 호두가 만나 짭쪼름하지만 고소하게!",
        nPrice = "5,800원",
        sPrice = "5,220원",
        badge = listOf("이벤트특가")
    )
    private val mainDishes = listOf(mainDish1, mainDish2)
    private val soupDishes = listOf(soupDish1, soupDish2)
    private val sideDishes = listOf(sideDish1, sideDish2)
    private val bests = listOf(
        BestResponse(
            categoryId = "17011000",
            name = "풍성한 고기반찬",
            items = mainDishes
        )
    )
    private val cartDto1 = CartDto("HBDEF", 1, true, 1661219851964, "오리 주물럭_반조리")
    private val cartDto2 = CartDto("HA6EE", 1, true, 1661219851964, "된장찌개")
    private val cartDto3 = CartDto("HBBCC", 1, true, 1661219851964, "새콤달콤 오징어무침")
    private val cartDtos = listOf(cartDto1, cartDto2, cartDto3)

    private lateinit var cartDataSource: CartDataSource
    private lateinit var bestDataSource: BestDataSource
    private lateinit var mainDishDataSource: MainDishDataSource
    private lateinit var soupDishDataSource: SoupDishDataSource
    private lateinit var sideDishDataSource: SideDishDataSource

    private lateinit var dishRepository: DishRepository

    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        cartDataSource = FakeCartDataSource(cartDtos.toMutableList())
        bestDataSource = FakeBestDataSource(bests)
        mainDishDataSource = FakeMainDishDataSource(mainDishes)
        soupDishDataSource = FakeSoupDishDataSource(soupDishes)
        sideDishDataSource = FakeSideDishDataSource(sideDishes)

        dishRepository = DishRepositoryImpl(
            cartDataSource,
            bestDataSource,
            mainDishDataSource,
            sideDishDataSource,
            soupDishDataSource,
            testDispatcher
        )
    }

    @Test
    fun getBestDishesTest() {
        runTest {
            val expected = Result.success(
                listOf(
                    BestItem(
                        name = "풍성한 고기반찬",
                        items = listOf(
                            Dish(
                                detailHash = "HBDEF",
                                imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/1155_ZIP_P_0081_T.jpg",
                                alt = "오리 주물럭_반조리",
                                deliveryType = listOf("새벽배송", "전국택배"),
                                title = "오리 주물럭_반조리",
                                description = "감질맛 나는 매콤한 양념",
                                discount = 20,
                                nPrice = 15800,
                                sPrice = 12640,
                                badge = listOf("런칭특가"),
                                isAdded = true
                            ),
                            Dish(
                                detailHash = "HF778",
                                imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                                alt = "소갈비찜",
                                deliveryType = listOf("새벽배송", "전국택배"),
                                title = "소갈비찜",
                                description = "촉촉하게 밴 양념이 일품",
                                discount = 10,
                                nPrice = 28900,
                                sPrice = 26010,
                                badge = listOf("이벤트특가", "메인특가"),
                                isAdded = false
                            )
                        )
                    )
                )
            )
            var actual: Result<List<BestItem>>? = null
            val collectJob = launch(testDispatcher) {
                dishRepository.getBestDishes().collect {
                    actual = it
                }
            }
            assertEquals(expected, actual)
            collectJob.cancel()
        }
    }

    @Test
    fun getDishesTest() {
        runTest {
            val mainDishExpected = Result.success(
                listOf(
                    Dish(
                        detailHash = "HBDEF",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/1155_ZIP_P_0081_T.jpg",
                        alt = "오리 주물럭_반조리",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "오리 주물럭_반조리",
                        description = "감질맛 나는 매콤한 양념",
                        discount = 20,
                        nPrice = 15800,
                        sPrice = 12640,
                        badge = listOf("런칭특가"),
                        isAdded = true
                    ),
                    Dish(
                        detailHash = "HF778",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                        alt = "소갈비찜",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "소갈비찜",
                        description = "촉촉하게 밴 양념이 일품",
                        discount = 10,
                        nPrice = 28900,
                        sPrice = 26010,
                        badge = listOf("이벤트특가", "메인특가"),
                        isAdded = false
                    )
                )
            )
            val soupDishExpected = Result.success(
                listOf(
                    Dish(
                        detailHash = "H72C3",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/soup/28_ZIP_P_1003_T.jpg",
                        alt = "한돈 돼지 김치찌개",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "한돈 돼지 김치찌개",
                        description = "김치찌개에는 역시 돼지고기",
                        discount = 10,
                        nPrice = 9300,
                        sPrice = 8370,
                        badge = listOf("이벤트특가"),
                        isAdded = false
                    ),
                    Dish(
                        detailHash = "HA6EE",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/soup/33_ZIP_P_1004_T.jpg",
                        alt = "된장찌개",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "된장찌개",
                        description = "특별하지 않아서 더 좋은 우리맛",
                        discount = 10,
                        nPrice = 8800,
                        sPrice = 7920,
                        badge = listOf("이벤트특가"),
                        isAdded = true
                    )
                )
            )
            val sideDishExpected = Result.success(
                listOf(
                    Dish(
                        detailHash = "HBBCC",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_T.jpg",
                        alt = "새콤달콤 오징어무침",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "새콤달콤 오징어무침",
                        description = "국내산 오징어를 새콤달콤하게",
                        discount = 20,
                        nPrice = 7500,
                        sPrice = 6000,
                        badge = listOf("런칭특가"),
                        isAdded = true
                    ),
                    Dish(
                        detailHash = "H1939",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/17_ZIP_P_6014_T.jpg",
                        alt = "호두 멸치볶음",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "호두 멸치볶음",
                        description = "잔멸치와 호두가 만나 짭쪼름하지만 고소하게!",
                        discount = 10,
                        nPrice = 5800,
                        sPrice = 5220,
                        badge = listOf("이벤트특가"),
                        isAdded = false
                    )
                )
            )

            var mainDishActual: Result<List<Dish>>? = null
            var soupDishActual: Result<List<Dish>>? = null
            var sideDishActual: Result<List<Dish>>? = null


            val collectJob = launch(testDispatcher) {
                dishRepository.getDishes(Source.MAIN).collect{
                    mainDishActual = it
                }
                dishRepository.getDishes(Source.SOUP).collect{
                    soupDishActual = it
                }
                dishRepository.getDishes(Source.SIDE).collect{
                    sideDishActual = it
                }
            }

            assertEquals(mainDishExpected,mainDishActual)
            assertEquals(soupDishExpected,soupDishActual)
            assertEquals(sideDishExpected,sideDishActual)

            collectJob.cancel()
        }
    }
}