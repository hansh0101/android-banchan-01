package co.kr.woowahan_banchan.data.datasource.remote.maindish

import co.kr.woowahan_banchan.data.model.remote.response.DishResponse

interface MainDishDataSource {
    suspend fun getMainDishes() : Result<List<DishResponse>>
}