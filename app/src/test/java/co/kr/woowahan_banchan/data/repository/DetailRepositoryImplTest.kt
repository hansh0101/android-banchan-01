package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.data.model.remote.response.DetailDataResponse
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
import co.kr.woowahan_banchan.data.repository.fakedatasource.FakeDetailDataSource
import co.kr.woowahan_banchan.data.repository.fakedatasource.FakeDetailDataSourceWithError
import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.entity.error.ErrorEntity
import co.kr.woowahan_banchan.domain.repository.DetailRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DetailRepositoryImplTest {

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

    lateinit var detailDataSource: DetailDataSource
    lateinit var detailDataSourceWithError: DetailDataSource

    lateinit var detailRepository: DetailRepository
    lateinit var detailRepositoryWithError: DetailRepository

    @Before
    fun setUp() {
        detailDataSource = FakeDetailDataSource(detailResponse)
        detailDataSourceWithError = FakeDetailDataSourceWithError()

        detailRepository = DetailRepositoryImpl(detailDataSource)
        detailRepositoryWithError = DetailRepositoryImpl(detailDataSourceWithError)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getDishInfoTest() {
        runTest {
            val expected = Result.success(
                DishInfo(
                    topImageUrl = "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                    thumbnailUrls = listOf(
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_T.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_S.jpg"
                    ),
                    productDescription = "촉촉하게 밴 양념이 일품",
                    point = 260,
                    deliveryInfo = "서울 경기 새벽 배송 / 전국 택배 배송",
                    deliveryFeeInfo = "2,500원 (40,000원 이상 구매 시 무료)",
                    prices = listOf(28900, 26010),
                    discount = 10,
                    detailImageUrls = listOf(
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D1.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/main/349_ZIP_P_0024_D2.jpg",
                        "http://public.codesquad.kr/jk/storeapp/data/pakage_regular.jpg"
                    )
                )
            )
            val actual = detailRepository.getDishInfo("HF778")
            assertEquals(expected, actual)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getDishInfoErrorTest() {
        runTest {
            val expected = Result.failure<DishInfo>(ErrorEntity.ConditionalError)
            val actual = detailRepositoryWithError.getDishInfo("HF778")
            assertEquals(expected, actual)
        }
    }
}