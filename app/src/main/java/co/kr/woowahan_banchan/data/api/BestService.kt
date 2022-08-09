package co.kr.woowahan_banchan.data.api

import co.kr.woowahan_banchan.data.model.remote.response.ApiResponse
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
import retrofit2.http.GET


interface BestService {
    @GET("/onban/best")
    suspend fun getBests() : ApiResponse<List<BestResponse>>
}