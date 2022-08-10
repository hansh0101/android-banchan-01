package co.kr.woowahan_banchan.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import co.kr.woowahan_banchan.data.database.dao.CartDao
import co.kr.woowahan_banchan.data.database.dao.HistoryDao
import co.kr.woowahan_banchan.data.database.dao.OrderDao
import co.kr.woowahan_banchan.data.database.dao.OrderItemDao
import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import co.kr.woowahan_banchan.data.model.local.OrderDto
import co.kr.woowahan_banchan.data.model.local.OrderItemDto

@Database(
    entities = [CartDto::class, HistoryDto::class, OrderDto::class, OrderItemDto::class],
    version = 1
)
abstract class BanchanDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun historyDao(): HistoryDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao
}
