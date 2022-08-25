package co.kr.woowahan_banchan.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 2
)
abstract class BanchanDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun historyDao(): HistoryDao
    abstract fun orderDao(): OrderDao
    abstract fun orderItemDao(): OrderItemDao

    companion object {
        val migration_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE 'ORDER' ADD COLUMN is_completed INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
