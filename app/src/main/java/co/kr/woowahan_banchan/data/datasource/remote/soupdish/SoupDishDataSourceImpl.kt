package co.kr.woowahan_banchan.data.datasource.remote.soupdish

import co.kr.woowahan_banchan.data.api.SoupDishService
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import co.kr.woowahan_banchan.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SoupDishDataSourceImpl @Inject constructor(
    private val service: SoupDishService,
    @IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : SoupDishDataSource {
    override suspend fun getSoupDishes(): Result<List<DishResponse>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                service.getSoupDishes().body
            }
        }
}