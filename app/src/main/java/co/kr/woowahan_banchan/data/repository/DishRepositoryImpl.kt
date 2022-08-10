package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.repository.DishRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.*
import javax.inject.Inject

class DishRepositoryImpl @Inject constructor(
    private val cartDataSource: CartDataSource,
    private val bestDataSource: BestDataSource
) : DishRepository {
    override suspend fun addToCart(hash: String, amount: Int, name: String) {
        cartDataSource.insertOrUpdateItems(
            listOf(CartDto(hash, amount, true, Date().time, name))
        )
    }

    override fun getBestDishes(): Flow<List<BestItem>> {
        val cartFlow = cartDataSource.getItems()
        return cartFlow.map { cartDtoList ->
            val apiResult = bestDataSource.getBests()
            when (apiResult.isSuccess) {
                true -> {
                    apiResult.getOrDefault(listOf()).map { bestDishes ->
                        BestItem(
                            bestDishes.name,
                            bestDishes.items.map { bestDish ->
                                bestDish.toEntity(cartDtoList.find { it.hash == bestDish.detailHash } != null)
                            }
                        )
                    }
                }
                false -> {
                    listOf()
                }
            }
        }
    }
}
