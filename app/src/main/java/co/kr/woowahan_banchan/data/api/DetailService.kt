package co.kr.woowahan_banchan.data.api

import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {
    @GET("/onban/detail/{hash}")
    suspend fun getDetail(
        @Path("hash") hash : String
    ) : DetailResponse
}