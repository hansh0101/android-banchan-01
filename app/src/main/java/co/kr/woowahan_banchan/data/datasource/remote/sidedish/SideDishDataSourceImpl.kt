package co.kr.woowahan_banchan.data.datasource.remote.sidedish

import co.kr.woowahan_banchan.data.api.SideDishService
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SideDishDataSourceImpl(
    private val service: SideDishService,
    private val coroutineDispatcher: CoroutineDispatcher
) : SideDishDataSource {
    override suspend fun getSideDishes(): Result<List<DishResponse>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                service.getSideDishes().body
            }
        }
}