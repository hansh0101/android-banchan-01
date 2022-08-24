package co.kr.woowahan_banchan.data.repository.fakedatasource.local.cart

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.EOFException
import okio.IOException

class FakeCartDataSourceWithError : CartDataSource {
    override fun getItems(): Flow<Result<List<CartDto>>> {
        return flow {
            emit(Result.failure(Exception().toErrorEntity()))
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