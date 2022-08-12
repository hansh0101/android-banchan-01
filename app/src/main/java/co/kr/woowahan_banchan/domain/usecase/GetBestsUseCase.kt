package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.repository.DishRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBestsUseCase @Inject constructor(
    private val repository: DishRepository
) {
    operator fun invoke() : Flow<List<BestItem>> {
        return repository.getBestDishes()
    }
}