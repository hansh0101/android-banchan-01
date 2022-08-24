package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
import java.net.UnknownHostException

class FakeDetailDataSourceWithError : DetailDataSource {
    override suspend fun getDetail(hash: String): Result<DetailResponse> {
        return Result.failure(UnknownHostException().toErrorEntity())
    }
}