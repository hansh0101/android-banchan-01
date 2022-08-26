package co.kr.woowahan_banchan.data.datasource.remote.maindish

import co.kr.woowahan_banchan.data.api.MainDishService
import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class MainDishDataSourceImplTest {

    private lateinit var mainDishService: MainDishService
    private lateinit var mainDishDataSource: MainDishDataSource
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        mainDishService = FakeMainDishService()
        mainDishDataSource = MainDishDataSourceImpl(mainDishService, testDispatcher)
    }

    @Test
    fun getMainDishesTest() {
        runTest {
            val expected = Result.success(
                listOf(
                    DishResponse(
                        detailHash = "HF778",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                        alt = "소갈비찜",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "소갈비찜",
                        description = "촉촉하게 밴 양념이 일품",
                        nPrice = "28,900원",
                        sPrice = "26,010원",
                        badge = listOf(
                            "이벤트특가",
                            "메인특가"
                        )
                    )
                )
            )
            val actual = mainDishDataSource.getMainDishes()
            assertEquals(expected, actual)
        }
    }
}

class FakeMainDishService : MainDishService {
    override suspend fun getMainDishes(): ApiResponse<List<DishResponse>> {
        return ApiResponse(
            statusCode = 200,
            body = listOf(
                DishResponse(
                    detailHash = "HF778",
                    imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                    alt = "소갈비찜",
                    deliveryType = listOf("새벽배송", "전국택배"),
                    title = "소갈비찜",
                    description = "촉촉하게 밴 양념이 일품",
                    nPrice = "28,900원",
                    sPrice = "26,010원",
                    badge = listOf(
                        "이벤트특가",
                        "메인특가"
                    )
                )
            )
        )
    }
}
