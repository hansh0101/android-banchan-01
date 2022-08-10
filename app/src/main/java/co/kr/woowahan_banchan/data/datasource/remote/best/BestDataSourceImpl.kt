package co.kr.woowahan_banchan.data.datasource.remote.best

import co.kr.woowahan_banchan.data.api.BestService
import co.kr.woowahan_banchan.data.model.remote.response.BestResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class BestDataSourceImpl(
    private val service: BestService,
    private val coroutineDispatcher: CoroutineDispatcher
) : BestDataSource {

    override suspend fun getBests(): Result<List<BestResponse>> =
        withContext(coroutineDispatcher) {
            runCatching {
                val response = service.getBests()
                if (response.statusCode != 200) return@withContext Result.failure(Exception("network err"))
                val body = response.body ?: return@withContext Result.failure(Exception("null response"))
                body
            }
        }
}