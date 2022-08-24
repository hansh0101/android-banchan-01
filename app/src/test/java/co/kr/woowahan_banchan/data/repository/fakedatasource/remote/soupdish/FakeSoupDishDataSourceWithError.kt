package co.kr.woowahan_banchan.data.repository.fakedatasource.remote.soupdish

import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import java.net.UnknownHostException

class FakeSoupDishDataSourceWithError : SoupDishDataSource {
    override suspend fun getSoupDishes(): Result<List<DishResponse>> {
        return Result.failure(UnknownHostException().toErrorEntity())
    }

}