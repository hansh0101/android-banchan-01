package co.kr.woowahan_banchan.data.datasource.remote.soupdish

import co.kr.woowahan_banchan.data.model.remote.response.DishResponse

interface SoupDishDataSource {
    suspend fun getSoupDishes() : Result<List<DishResponse>>
}