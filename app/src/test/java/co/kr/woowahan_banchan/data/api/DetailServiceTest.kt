package co.kr.woowahan_banchan.data.api

import co.kr.woowahan_banchan.data.model.remote.response.DetailDataResponse
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
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

class DetailServiceTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var detailService: DetailService

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        detailService = Retrofit.Builder()
            .baseUrl(mockWebServer.url(""))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DetailService::class.java)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getDetailTest() {
        runTest {
            val jsonResponse =
                File("src/test/java/co/kr/woowahan_banchan/resource/detail_hbbcc.json").readText()
            val response = MockResponse().setBody(jsonResponse)
            mockWebServer.enqueue(response)

            val expected = DetailResponse(
                hash = "HBBCC",
                data = DetailDataResponse(
                    topImageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_T.jpg",
                    thumbnailUrls = listOf(
                        "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_T.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_S.jpg"
                    ),
                    productDescription = "국내산 오징어를 새콤달콤하게",
                    point = "60원",
                    deliveryInfo = "서울 경기 새벽 배송 / 전국 택배 배송",
                    deliveryFee = "2,500원 (40,000원 이상 구매 시 무료)",
                    prices = listOf("7,500원", "6,000원"),
                    detailSection = listOf(
                        "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_D1.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/side/48_ZIP_P_5008_D2.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/pakage_small.jpg"
                    )
                )
            )
            val actual = detailService.getDetail("HBBCC")
            assertEquals(expected, actual)
        }
    }
}