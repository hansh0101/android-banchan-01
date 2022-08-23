package co.kr.woowahan_banchan.data.api

import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File

class MainDishServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var mainDishService: MainDishService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mainDishService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MainDishService::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getMainDishesTest() {
        runTest {
            val jsonResponse =
                File("src/test/java/co/kr/woowahan_banchan/resource/dishes.json").readText()
            val response = MockResponse().setBody(jsonResponse)
            mockWebServer.enqueue(response)

            val expected = ApiResponse(
                statusCode = 200,
                body = listOf(
                    DishResponse(
                        detailHash = "HBDEF",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/1155_ZIP_P_0081_T.jpg",
                        alt = "오리 주물럭_반조리",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "오리 주물럭_반조리",
                        description = "감칠맛 나는 매콤한 양념",
                        nPrice = "15,800원",
                        sPrice = "12,640원",
                        badge = listOf(
                            "런칭특가"
                        )
                    ),
                    DishResponse(
                        detailHash = "HF778",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                        alt = "소갈비찜",
                        deliveryType = listOf("새벽배송", "전국택배"),
                        title = "소갈비찜",
                        description = "촉촉하게 벤 양념이 일품",
                        nPrice = "28,900원",
                        sPrice = "26,010원",
                        badge = listOf(
                            "이벤트특가",
                            "메인특가"
                        )
                    )
                )
            )
            val actual = mainDishService.getMainDishes()
            assertEquals(expected, actual)
        }
    }
}