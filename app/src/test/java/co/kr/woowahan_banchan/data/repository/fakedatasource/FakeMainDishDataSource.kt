package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSource
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse

class FakeMainDishDataSource(
    private val dishes: List<DishResponse>
) : MainDishDataSource {
    override suspend fun getMainDishes(): Result<List<DishResponse>> {
        return Result.success(dishes)
    }
}