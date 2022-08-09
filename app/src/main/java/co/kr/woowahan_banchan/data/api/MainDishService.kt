package co.kr.woowahan_banchan.data.api

import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import retrofit2.http.GET

interface MainDishService {
    @GET("/onban/main")
    suspend fun getMainDishes() : ApiResponse<List<DishResponse>>
}