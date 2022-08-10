package co.kr.woowahan_banchan.di

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
}