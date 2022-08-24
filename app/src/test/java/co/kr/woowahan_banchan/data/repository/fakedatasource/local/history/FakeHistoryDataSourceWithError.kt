package co.kr.woowahan_banchan.data.repository.fakedatasource.local.history

import co.kr.woowahan_banchan.data.datasource.local.history.HistoryDataSource
import co.kr.woowahan_banchan.data.extension.toErrorEntity
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.InterruptedIOException

class FakeHistoryDataSourceWithError: HistoryDataSource {
    override fun getItems(): Flow<Result<List<HistoryDto>>> {
        return flow { emit(Result.failure(InterruptedIOException().toErrorEntity())) }
    }

    override fun getPreviewItems(): Flow<Result<List<HistoryDto>>> {
        return flow { emit(Result.failure(ClassNotFoundException().toErrorEntity())) }
    }

    override suspend fun insertItem(hash: String, name: String): Result<Unit> {
        return Result.failure(IllegalThreadStateException().toErrorEntity())
    }
}