package co.kr.woowahan_banchan.data.api

import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import retrofit2.http.GET

interface SoupDishService {
    @GET("/onban/soup")
    suspend fun getSoupDishes() : ApiResponse<List<DishResponse>>
}