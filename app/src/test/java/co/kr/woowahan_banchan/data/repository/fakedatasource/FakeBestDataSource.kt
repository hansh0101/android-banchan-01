package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse

class FakeBestDataSource(
    private val bestResponses: List<BestResponse>
) : BestDataSource {
    override suspend fun getBests(): Result<List<BestResponse>> {
        return Result.success(bestResponses)
    }
}