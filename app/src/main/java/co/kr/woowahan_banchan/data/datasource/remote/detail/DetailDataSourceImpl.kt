package co.kr.woowahan_banchan.data.datasource.remote.detail

import co.kr.woowahan_banchan.data.api.DetailService
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DetailResponse
import co.kr.woowahan_banchan.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DetailDataSourceImpl @Inject constructor(
    private val service: DetailService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : DetailDataSource {
    override suspend fun getDetail(hash: String): Result<DetailResponse> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                service.getDetail(hash)
            }
        }
}