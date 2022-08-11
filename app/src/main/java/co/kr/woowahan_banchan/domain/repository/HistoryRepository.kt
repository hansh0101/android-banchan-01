package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getHistories(previewMode: Boolean): Flow<List<HistoryItem>>
}