package co.kr.woowahan_banchan.di

import android.content.Context
import androidx.room.Room
import co.kr.woowahan_banchan.data.database.BanchanDatabase
import co.kr.woowahan_banchan.data.database.dao.CartDao
import co.kr.woowahan_banchan.data.database.dao.HistoryDao
import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.database.dao.OrderItemDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideBanchanDatabase(@ApplicationContext context: Context): BanchanDatabase =
        Room.databaseBuilder(context, BanchanDatabase::class.java, "banchan.db")
            .addMigrations(BanchanDatabase.migration_1_2)
            .build()

    @Provides
    @Singleton
    fun provideCartDao(database: BanchanDatabase): CartDao = database.cartDao()

    @Provides
    @Singleton
    fun provideHistoryDao(database: BanchanDatabase): HistoryDao = database.historyDao()

    @Provides
    @Singleton
    fun provideOrderDao(database: BanchanDatabase): OrderDao = database.orderDao()

    @Provides
    @Singleton
    fun provideOrderItemDao(database: BanchanDatabase): OrderItemDao = database.orderItemDao()
}