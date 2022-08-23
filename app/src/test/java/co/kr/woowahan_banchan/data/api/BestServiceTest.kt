package co.kr.woowahan_banchan.data.api

import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
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

class BestServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var bestService: BestService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        bestService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(BestService::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getBestsTest() {
        runTest {
            val jsonResponse =
                File("src/test/java/co/kr/woowahan_banchan/resource/best_items.json").readText()
            val response = MockResponse().setBody(jsonResponse)
            mockWebServer.enqueue(response)

            val expected = ApiResponse(
                statusCode = 200,
                body = listOf(
                    BestResponse(
                        categoryId = "17011000",
                        name = "풍성한 고기반찬",
                        items = listOf(
                            DishResponse(
                                detailHash = "HBDEF",
                                imageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/1155_ZIP_P_0081_T.jpg",
                                alt = "오리 주물럭_반조리",
                                deliveryType = listOf("새벽배송", "전국택배"),
                                title = "오리 주물럭_반조리",
                                description = "감질맛 나는 매콤한 양념",
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
                                    "이벤트특가"
                                )
                            )
                        )
                    ),
                    BestResponse(
                        categoryId = "17010200",
                        name = "편리한 반찬세트",
                        items = listOf(
                            DishResponse(
                                detailHash = "H1939",
                                imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_T.jpg",
                                alt = "호두 멸치볶음",
                                deliveryType = listOf("새벽배송", "전국택배"),
                                title = "호두 멸치볶음",
                                description = "잔멸치와 호두가 만나 짭쪼름하지만 고소하게!",
                                nPrice = "5,800원",
                                sPrice = "5,220원",
                                badge = listOf(
                                    "이벤트특가"
                                )
                            ),
                            DishResponse(
                                detailHash = "H8EA5",
                                imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_T.jpg",
                                alt = "한돈 매콤 안심 장조림",
                                deliveryType = listOf("새벽배송", "전국택배"),
                                title = "한돈 매콤 안심 장조림",
                                description = "부드러운 한돈 안심살의 매콤함",
                                nPrice = "6,900",
                                sPrice = "6,210원",
                                badge = listOf(
                                    "이벤트특가"
                                )
                            )
                        )
                    )
                )
            )

            val actual = bestService.getBests()
            assertEquals(expected, actual)
        }
    }
}