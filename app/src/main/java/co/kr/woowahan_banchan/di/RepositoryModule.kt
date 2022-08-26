package co.kr.woowahan_banchan.di

import co.kr.woowahan_banchan.data.repository.*
import co.kr.woowahan_banchan.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun provideDishRepository(
        dishRepositoryImpl: DishRepositoryImpl
    ): DishRepository

    @Binds
    @Singleton
    abstract fun provideHistoryRepository(
        historyRepositoryImpl: HistoryRepositoryImpl
    ): HistoryRepository

    @Binds
    @Singleton
    abstract fun provideDetailRepository(
        detailRepositoryImpl: DetailRepositoryImpl
    ): DetailRepository

    @Binds
    @Singleton
    abstract fun provideOrderHistoryRepository(
        orderHistoryRepositoryImpl: OrderHistoryRepositoryImpl
    ): OrderHistoryRepository

    @Binds
    @Singleton
    abstract fun provideCartRepository(
        cartRepositoryImpl: CartRepositoryImpl
    ): CartRepository
}