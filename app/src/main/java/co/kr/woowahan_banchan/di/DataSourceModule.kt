package co.kr.woowahan_banchan.di

import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSource
import co.kr.woowahan_banchan.data.datasource.local.cart.CartDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.local.history.HistoryDataSource
import co.kr.woowahan_banchan.data.datasource.local.history.HistoryDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.local.order.OrderDataSource
import co.kr.woowahan_banchan.data.datasource.local.order.OrderDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.local.orderitem.OrderItemDataSource
import co.kr.woowahan_banchan.data.datasource.local.orderitem.OrderItemDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSource
import co.kr.woowahan_banchan.data.datasource.remote.best.BestDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.maindish.MainDishDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.sidedish.SideDishDataSourceImpl
import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSource
import co.kr.woowahan_banchan.data.datasource.remote.soupdish.SoupDishDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    @Singleton
    abstract fun provideCartDataSource(
        cartDataSourceImpl: CartDataSourceImpl
    ): CartDataSource

    @Binds
    @Singleton
    abstract fun provideHistoryDataSource(
        historyDataSourceImpl: HistoryDataSourceImpl
    ): HistoryDataSource

    @Binds
    @Singleton
    abstract fun provideOrderDataSource(
        orderDataSourceImpl: OrderDataSourceImpl
    ): OrderDataSource

    @Binds
    @Singleton
    abstract fun provideOrderItemDataSource(
        orderItemDataSourceImpl: OrderItemDataSourceImpl
    ): OrderItemDataSource

    @Binds
    @Singleton
    abstract fun provideBestDataSource(
        bestDataSourceImpl: BestDataSourceImpl
    ): BestDataSource

    @Binds
    @Singleton
    abstract fun provideDetailDataSource(
        detailDataSourceImpl: DetailDataSourceImpl
    ): DetailDataSource


    @Binds
    @Singleton
    abstract fun provideMainDishDataSource(
        mainDishDataSourceImpl: MainDishDataSourceImpl
    ): MainDishDataSource


    @Binds
    @Singleton
    abstract fun provideSideDishDataSource(
        sideDishDataSourceImpl: SideDishDataSourceImpl
    ): SideDishDataSource


    @Binds
    @Singleton
    abstract fun provideSoupDishDataSource(
        soupDishDataSourceImpl: SoupDishDataSourceImpl
    ): SoupDishDataSource
}