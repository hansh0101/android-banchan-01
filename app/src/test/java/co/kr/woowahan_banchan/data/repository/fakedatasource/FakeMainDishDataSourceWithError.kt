package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import java.io.InterruptedIOException

class FakeMainDishDataSourceWithError: MainDishDataSource {
    override suspend fun getMainDishes(): Result<List<DishResponse>> {
        return Result.failure(InterruptedIOException().toErrorEntity())
    }
}