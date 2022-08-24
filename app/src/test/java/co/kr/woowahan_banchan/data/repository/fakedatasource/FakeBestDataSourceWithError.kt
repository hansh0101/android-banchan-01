package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
import okio.IOException

class FakeBestDataSourceWithError : BestDataSource {
    override suspend fun getBests(): Result<List<BestResponse>> {
        return Result.failure(IOException().toErrorEntity())
    }
}