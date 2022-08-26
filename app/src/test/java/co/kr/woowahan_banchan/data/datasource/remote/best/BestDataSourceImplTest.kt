package co.kr.woowahan_banchan.data.datasource.remote.best

import co.kr.woowahan_banchan.data.api.BestService
import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class BestDataSourceImplTest {

    private lateinit var bestService: BestService
    private lateinit var bestDataSource: BestDataSource
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        bestService = FakeBestService()
        bestDataSource = BestDataSourceImpl(bestService, testDispatcher)
    }

    @Test
    fun getBestsTest() {
        runTest {
            val expected = Result.success(
                listOf(
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
                                badge = listOf("런칭특가")
                            )
                        )
                    )
                )
            )

            val actual = bestDataSource.getBests()
            assertEquals(expected, actual)
        }
    }
}

class FakeBestService : BestService {
    override suspend fun getBests(): ApiResponse<List<BestResponse>> {
        return ApiResponse(
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
                            badge = listOf("런칭특가")
                        )
                    )
                )
            )
        )
    }
}