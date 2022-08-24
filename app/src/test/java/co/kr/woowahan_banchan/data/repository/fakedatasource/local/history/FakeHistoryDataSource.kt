package co.kr.woowahan_banchan.data.repository.fakedatasource.local.history

import co.kr.woowahan_banchan.data.datasource.local.history.HistoryDataSource
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeHistoryDataSource(initHistoryDtos: List<HistoryDto>): HistoryDataSource {
    val historyDtos = initHistoryDtos.toMutableList()

    override fun getItems(): Flow<Result<List<HistoryDto>>> {
        return flow { emit(Result.success(historyDtos)) }
    }

    override fun getPreviewItems(): Flow<Result<List<HistoryDto>>> {
        return flow { emit(Result.success(historyDtos)) }
    }

    override suspend fun insertItem(hash: String, name: String): Result<Unit> {
        return Result.success(Unit)
    }
}