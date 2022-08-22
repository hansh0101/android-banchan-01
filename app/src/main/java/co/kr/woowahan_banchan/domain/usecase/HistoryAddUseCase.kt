package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.repository.HistoryRepository
import javax.inject.Inject

class HistoryAddUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(hash: String, name: String): Result<Unit> =
        historyRepository.addToHistory(hash, name)
}