package co.kr.woowahan_banchan.di

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSource
import co.kr.woowahan_banchan.data.repository.DishRepositoryImpl
import co.kr.woowahan_banchan.domain.repository.DishRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideDishRepository(
        cartDataSource: CartDataSource,
        bestDataSource: BestDataSource,
        mainDishDataSource: MainDishDataSource,
        sideDishDataSource: SideDishDataSource,
        soupDishDataSource: SoupDishDataSource,
        @DefaultDispatcher coroutineDispatcher: CoroutineDispatcher
    ): DishRepository = DishRepositoryImpl(
        cartDataSource,
        bestDataSource,
        mainDishDataSource,
        sideDishDataSource,
        soupDishDataSource,
        coroutineDispatcher
    )
}