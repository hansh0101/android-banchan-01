package co.kr.woowahan_banchan.data.datasource.remote.best

import co.kr.woowahan_banchan.data.api.BestService
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BestDataSourceImpl(
    private val service: BestService,
    private val coroutineDispatcher: CoroutineDispatcher
) : BestDataSource {
    override suspend fun getBests(): Result<List<BestResponse>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                service.getBests().body
            }
        }
}