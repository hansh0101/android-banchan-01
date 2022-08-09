package co.kr.woowahan_banchan.data.model.remote.response

data class ApiResponse<T>(
    val statusCode : Int,
    val body : T? = null,
)
