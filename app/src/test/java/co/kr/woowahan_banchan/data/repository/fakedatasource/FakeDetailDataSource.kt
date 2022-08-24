package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.data.model.remote.response.DetailDataResponse
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse

class FakeDetailDataSource(
    private val detailResponse: DetailResponse
) : DetailDataSource {
    override suspend fun getDetail(hash: String): Result<DetailResponse> {
        return Result.success(detailResponse)
    }
}