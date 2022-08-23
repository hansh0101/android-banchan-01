package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import kotlinx.coroutines.flow.Flow

interface DishRepository {
    fun getBestDishes(): Flow<Result<List<BestItem>>>
    fun getDishes(source: Source): Flow<Result<List<Dish>>>
}

enum class Source {
    MAIN, SIDE, SOUP
}