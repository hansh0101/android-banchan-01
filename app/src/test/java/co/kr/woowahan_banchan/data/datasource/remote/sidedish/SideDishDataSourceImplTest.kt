package co.kr.woowahan_banchan.data.datasource.remote.sidedish

import co.kr.woowahan_banchan.data.api.SideDishService
import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class SideDishDataSourceImplTest {

    private lateinit var sideDishService: SideDishService
    private lateinit var sideDishDataSource: SideDishDataSource
    private val testDispatcher = UnconfinedTestDispatcher()


    @Before
    fun setUp() {
        sideDishService = FakeSideDishService()
        sideDishDataSource = SideDishDataSourceImpl(sideDishService, testDispatcher)
    }

    @Test
    fun getSideDishesTest() {
        runTest {
            val expected = Result.success(
                listOf(
                    DishResponse(
                        detailHash = "H602F",
                        imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/84_ZIP_P_6006_T.jpg",
                        alt = "야채 어묵볶음",
                        deliveryType = listOf(
                            "새벽배송",
                            "전국택배"
                        ),
                        title = "야채 어묵볶음",
                        description = "첨가물 없는 순수어묵과 야채의 만남",
                        nPrice = "4,900원",
                        sPrice = "4,410원",
                        badge = listOf(
                            "이벤트특가"
                        )
                    )
                )
            )
            val actual = sideDishDataSource.getSideDishes()
            assertEquals(expected, actual)
        }
    }
}

class FakeSideDishService : SideDishService {
    override suspend fun getSideDishes(): ApiResponse<List<DishResponse>> {
        return ApiResponse(
            statusCode = 200,
            body = listOf(
                DishResponse(
                    detailHash = "H602F",
                    imageUrl = "http://public.codesquad.kr/jk/storeapp/data/side/84_ZIP_P_6006_T.jpg",
                    alt = "야채 어묵볶음",
                    deliveryType = listOf(
                        "새벽배송",
                        "전국택배"
                    ),
                    title = "야채 어묵볶음",
                    description = "첨가물 없는 순수어묵과 야채의 만남",
                    nPrice = "4,900원",
                    sPrice = "4,410원",
                    badge = listOf(
                        "이벤트특가"
                    )
                )
            )
        )
    }
}