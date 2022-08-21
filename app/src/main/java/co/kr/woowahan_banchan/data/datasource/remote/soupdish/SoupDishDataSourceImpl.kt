package co.kr.woowahan_banchan.data.datasource.remote.soupdish

import co.kr.woowahan_banchan.data.api.SoupDishService
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class SoupDishDataSourceImpl(
    private val service: SoupDishService,
    private val coroutineDispatcher: CoroutineDispatcher
) : SoupDishDataSource {
    override suspend fun getSoupDishes(): Result<List<DishResponse>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                service.getSoupDishes().body
            }
        }
}