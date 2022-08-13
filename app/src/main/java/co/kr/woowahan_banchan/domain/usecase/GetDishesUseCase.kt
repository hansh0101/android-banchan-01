package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.repository.DishRepository
import co.kr.woowahan_banchan.domain.repository.Source
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDishesUseCase @Inject constructor(
    private val repository: DishRepository
) {
    operator fun invoke(source: Source) : Flow<List<Dish>> {
        return repository.getDishes(source)
    }
}