package co.kr.woowahan_banchan.data.datasource.remote.detail

import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse

interface DetailDataSource {
    suspend fun getDetail(hash : String) : Result<DetailResponse>
}