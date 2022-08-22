package co.kr.woowahan_banchan.data.datasource.remote.maindish

import co.kr.woowahan_banchan.data.api.MainDishService
import co.kr.woowahan_banchan.data.extension.runCatchingErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class MainDishDataSourceImpl(
    private val service: MainDishService,
    private val coroutineDispatcher: CoroutineDispatcher
) : MainDishDataSource {
    override suspend fun getMainDishes(): Result<List<DishResponse>> =
        withContext(coroutineDispatcher) {
            runCatchingErrorEntity {
                service.getMainDishes().body
            }
        }
}