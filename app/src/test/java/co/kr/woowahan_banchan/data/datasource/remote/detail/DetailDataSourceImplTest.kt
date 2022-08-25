package co.kr.woowahan_banchan.data.datasource.remote.detail

import co.kr.woowahan_banchan.data.api.DetailService
import co.kr.woowahan_banchan.data.model.remote.response.DetailDataResponse
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailDataSourceImplTest {

    private lateinit var detailService: DetailService
    private lateinit var detailDataSource: DetailDataSource
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        detailService = FakeDetailService()
        detailDataSource = DetailDataSourceImpl(detailService, testDispatcher)
    }

    @Test
    fun getDetailTest() {
        runTest {
            val firstExpected = Result.success(
                DetailResponse(
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
            )
            val secondExpected = Result.success(
                DetailResponse(
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
            )

            val firstActual = detailDataSource.getDetail("HF778")
            assertEquals(firstExpected, firstActual)

            val secondActual = detailDataSource.getDetail("HBBCC")
            assertEquals(secondExpected, secondActual)
        }
    }

    @Test
    fun getDetailExceptionTest() {
        runTest {
            val failureExpected = Result.failure<DetailResponse>(ErrorEntity.UnknownError)
            val failureActual = detailDataSource.getDetail("aaaaa")
            assertEquals(failureExpected, failureActual)
        }
    }
}

class FakeDetailService : DetailService {
    override suspend fun getDetail(hash: String): DetailResponse {
        val fakeDetails = listOf<DetailResponse>(
            DetailResponse(
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
            ),
            DetailResponse(
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
        )
        return fakeDetails.find { it.hash == hash } ?: throw Exception()
    }
}

