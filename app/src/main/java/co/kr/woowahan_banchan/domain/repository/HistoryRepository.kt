package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    suspend fun addToHistory(hash: String, name: String): Result<Unit>
    fun getHistories(previewMode: Boolean): Flow<Result<List<HistoryItem>>>
}