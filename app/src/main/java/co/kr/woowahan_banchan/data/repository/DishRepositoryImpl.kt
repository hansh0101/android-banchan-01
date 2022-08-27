package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSource
import co.kr.woowahan_banchan.di.DefaultDispatcher
import co.kr.woowahan_banchan.domain.entity.dish.BestItem
import co.kr.woowahan_banchan.domain.entity.dish.Dish
import co.kr.woowahan_banchan.domain.repository.DishRepository
import co.kr.woowahan_banchan.domain.repository.Source
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DishRepositoryImpl @Inject constructor(
    private val cartDataSource: CartDataSource,
    private val bestDataSource: BestDataSource,
    private val mainDishDataSource: MainDishDataSource,
    private val sideDishDataSource: SideDishDataSource,
    private val soupDishDataSource: SoupDishDataSource,
    @DefaultDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : DishRepository {
    override fun getBestDishes(): Flow<Result<List<BestItem>>> {
        return cartDataSource.getItems().map { result ->
            result.mapCatching { cartDtoList ->
                bestDataSource.getBests()
                    .mapCatching { list ->
                        list.map { bestDishes ->
                            BestItem(
                                bestDishes.name,
                                bestDishes.items.map { bestDish ->
                                    bestDish.toEntity(cartDtoList.find { it.hash == bestDish.detailHash } != null)
                                }
                            )
                        }
                    }.getOrThrow()
            }.recoverCatching {
                bestDataSource.getBests()
                    .mapCatching {
                        it.map { bestDishes ->
                            BestItem(
                                bestDishes.name,
                                bestDishes.items.map { bestDish ->
                                    bestDish.toEntity(false)
                                }
                            )
                        }
                    }.getOrThrow()
            }
        }.catch {
            emit(Result.failure(it))
        }.flowOn(coroutineDispatcher)
    }

    override fun getDishes(source: Source): Flow<Result<List<Dish>>> {
        return cartDataSource.getItems().map { result ->
            result.mapCatching { cartDtoList ->
                when (source) {
                    Source.MAIN -> mainDishDataSource.getMainDishes()
                    Source.SIDE -> sideDishDataSource.getSideDishes()
                    Source.SOUP -> soupDishDataSource.getSoupDishes()
                }.mapCatching {
                    it.map { dish ->
                        dish.toEntity(cartDtoList.find { it.hash == dish.detailHash } != null)
                    }
                }.getOrThrow()
            }.recoverCatching {
                when (source) {
                    Source.MAIN -> mainDishDataSource.getMainDishes()
                    Source.SIDE -> sideDishDataSource.getSideDishes()
                    Source.SOUP -> soupDishDataSource.getSoupDishes()
                }.mapCatching {
                    it.map { dish ->
                        dish.toEntity(false)
                    }
                }.getOrThrow()
            }
        }.flowOn(coroutineDispatcher)
    }
}
