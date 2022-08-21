package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSource
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.repository.DishRepository
import co.kr.woowahan_banchan.domain.repository.Source
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DishRepositoryImpl @Inject constructor(
    private val cartDataSource: CartDataSource,
    private val bestDataSource: BestDataSource,
    private val mainDishDataSource: MainDishDataSource,
    private val sideDishDataSource: SideDishDataSource,
    private val soupDishDataSource: SoupDishDataSource,
    private val coroutineDispatcher: CoroutineDispatcher
) : DishRepository {
    override fun getBestDishes(): Flow<List<BestItem>> {
        return cartDataSource.getItems().map { cartDtoList ->
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
        }.flowOn(coroutineDispatcher)
    }

    override fun getDishes(source: Source): Flow<List<Dish>> {
        return cartDataSource.getItems().map { cartDtoList ->
            val apiResult = when (source) {
                Source.MAIN -> mainDishDataSource.getMainDishes()
                Source.SIDE -> sideDishDataSource.getSideDishes()
                Source.SOUP -> soupDishDataSource.getSoupDishes()
            }
            when (apiResult.isSuccess) {
                true -> {
                    apiResult.getOrDefault(listOf()).map { dish ->
                        dish.toEntity(cartDtoList.find { it.hash == dish.detailHash } != null)
                    }
                }
                false -> {
                    listOf()
                }
            }
        }.flowOn(coroutineDispatcher)
    }
}
