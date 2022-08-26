package co.kr.woowahan_banchan.data.datasource.remote.best

import co.kr.woowahan_banchan.data.api.BestService
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
import co.kr.woowahan_banchan.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BestDataSourceImpl @Inject constructor(
    private val service: BestService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : BestDataSource {
    override suspend fun getBests(): Result<List<BestResponse>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                service.getBests().body
            }
        }
}