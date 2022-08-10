package co.kr.woowahan_banchan.data.datasource.remote.best

import co.kr.woowahan_banchan.data.model.remote.response.BestResponse

interface BestDataSource{
    suspend fun getBests() : Result<List<BestResponse>>
}