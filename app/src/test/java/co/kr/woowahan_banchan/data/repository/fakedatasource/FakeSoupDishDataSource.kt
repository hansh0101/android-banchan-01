package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSource
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse

class FakeSoupDishDataSource(
    private val dishes: List<DishResponse>
) : SoupDishDataSource {
    override suspend fun getSoupDishes(): Result<List<DishResponse>> {
        return Result.success(dishes)
    }
}