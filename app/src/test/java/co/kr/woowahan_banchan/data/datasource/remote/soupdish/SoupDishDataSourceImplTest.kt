package co.kr.woowahan_banchan.data.datasource.remote.soupdish

import co.kr.woowahan_banchan.data.api.SoupDishService
import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SoupDishDataSourceImplTest {

    private lateinit var soupDishService: SoupDishService
    private lateinit var soupDishDataSource: SoupDishDataSource

    @Before
    fun setUp() {
        soupDishService = FakeSoupDishService()
        soupDishDataSource = SoupDishDataSourceImpl(soupDishService, Dispatchers.IO)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getSoupDishes() {
        runTest {
            val expected = Result.success(
                listOf(
                    DishResponse(
                        detailHash = "H72C3",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/soup/28_ZIP_P_1003_T.jpg",
                        alt = "한돈 돼지 김치찌개",
                        deliveryType = listOf(
                            "새벽배송",
                            "전국택배"
                        ),
                        title = "한돈 돼지 김치찌개",
                        description = "김치찌개에는 역시 돼지고기",
                        nPrice = "9,300원",
                        sPrice = "8,370원",
                        badge = listOf(
                            "이벤트특가"
                        )
                    )
                )
            )
            val actual = soupDishDataSource.getSoupDishes()
            assertEquals(expected, actual)
        }
    }
}

class FakeSoupDishService : SoupDishService {
    override suspend fun getSoupDishes(): ApiResponse<List<DishResponse>> {
        return ApiResponse(
            statusCode = 200,
            body = listOf(
                DishResponse(
                    detailHash = "H72C3",
                    imageUrl = "http://public.codesquad.kr/jk/storeapp/data/soup/28_ZIP_P_1003_T.jpg",
                    alt = "한돈 돼지 김치찌개",
                    deliveryType = listOf(
                        "새벽배송",
                        "전국택배"
                    ),
                    title = "한돈 돼지 김치찌개",
                    description = "김치찌개에는 역시 돼지고기",
                    nPrice = "9,300원",
                    sPrice = "8,370원",
                    badge = listOf(
                        "이벤트특가"
                    )
                )
            )
        )
    }
}