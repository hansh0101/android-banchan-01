package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.repository.DishRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetBestsUseCase @Inject constructor(
    private val repository: DishRepository
) {
    operator fun invoke(): Flow<Result<List<BestItem>>> {
        return repository.getBestDishes()
            .map { result ->
                result.mapCatching {
                    it.toMutableList().apply {
                        add(0, BestItem("title", listOf()))
                    }
                }
            }
    }
}