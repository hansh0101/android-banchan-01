package co.kr.woowahan_banchan.data.repository.fakedatasource

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.EOFException
import okio.IOException

class FakeCartDataSourceWithError : CartDataSource {
    override fun getItems(): Flow<List<CartDto>> {
        return flow {
            throw Exception().toErrorEntity()
        }
    }

    override suspend fun insertOrUpdateItems(items: List<CartDto>): Result<Unit> {
        return Result.failure(IOException().toErrorEntity())
    }

    override suspend fun deleteItems(ids: List<String>): Result<Unit> {
        return Result.failure(EOFException().toErrorEntity())
    }

    override suspend fun getAmount(hash: String): Result<Int> {
        return Result.failure(IllegalStateException().toErrorEntity())
    }
}