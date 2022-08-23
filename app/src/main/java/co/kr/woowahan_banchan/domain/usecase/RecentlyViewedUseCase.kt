package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import co.kr.woowahan_banchan.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecentlyViewedUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    operator fun invoke(previewMode: Boolean): Flow<Result<List<HistoryItem>>> =
        historyRepository.getHistories(previewMode)
}