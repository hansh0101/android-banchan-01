package co.kr.woowahan_banchan.data.repository.fakedatasource.remote.sidedish

import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSource
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse

class FakeSideDishDataSource(
    private val dishes: List<DishResponse>
) : SideDishDataSource {
    override suspend fun getSideDishes(): Result<List<DishResponse>> {
        return Result.success(dishes)
    }
}