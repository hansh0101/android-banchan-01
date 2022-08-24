package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.remote.response.DishResponse
import java.nio.channels.InterruptedByTimeoutException

class FakeSideDishDataSourceWithError : SideDishDataSource {
    override suspend fun getSideDishes(): Result<List<DishResponse>> {
        return Result.failure(InterruptedByTimeoutException().toErrorEntity())
    }

}