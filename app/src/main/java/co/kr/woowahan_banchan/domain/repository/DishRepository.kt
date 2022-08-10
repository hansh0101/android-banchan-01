package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import kotlinx.coroutines.flow.Flow

interface DishRepository {
    suspend fun addToCart(hash: String, amount: Int, name: String)
    fun getBestDishes(): Flow<List<BestItem>>
}