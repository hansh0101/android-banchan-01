package co.kr.woowahan_banchan.di

import co.kr.woowahan_banchan.data.api.*
import co.kr.woowahan_banchan.data.database.dao.CartDao
import co.kr.woowahan_banchan.data.database.dao.HistoryDao
import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.database.dao.OrderItemDao
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Provides
    @Singleton
    fun provideCartDataSource(
        cartDao: CartDao,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): CartDataSource = CartDataSourceImpl(cartDao, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideHistoryDataSource(
        historyDao: HistoryDao,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): HistoryDataSource = HistoryDataSourceImpl(historyDao, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideOrderDataSource(
        orderDao: OrderDao,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): OrderDataSource = OrderDataSourceImpl(orderDao, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideOrderItemDataSource(
        orderItemDao: OrderItemDao,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): OrderItemDataSource = OrderItemDataSourceImpl(orderItemDao, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideBestDataSource(
        service: BestService,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): BestDataSource = BestDataSourceImpl(service, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideDetailDataSource(
        service: DetailService,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): DetailDataSource = DetailDataSourceImpl(service, coroutineDispatcher)


    @Provides
    @Singleton
    fun provideMainDishDataSource(
        service: MainDishService,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): MainDishDataSource = MainDishDataSourceImpl(service, coroutineDispatcher)


    @Provides
    @Singleton
    fun provideSideDishDataSource(
        service: SideDishService,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): SideDishDataSource = SideDishDataSourceImpl(service, coroutineDispatcher)


    @Provides
    @Singleton
    fun provideSoupDishDataSource(
        service: SoupDishService,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher
    ): SoupDishDataSource = SoupDishDataSourceImpl(service, coroutineDispatcher)
}