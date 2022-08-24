package co.kr.woowahan_banchan.data.datasource.local.history

import co.kr.woowahan_banchan.data.model.local.HistoryDto
import kotlinx.coroutines.flow.Flow


interface HistoryDataSource {
    fun getItems(): Flow<Result<List<HistoryDto>>>
    fun getPreviewItems(): Flow<Result<List<HistoryDto>>>
    suspend fun insertItem(hash: String, name: String): Result<Unit>
}