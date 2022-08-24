package co.kr.woowahan_banchan.data.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migration {
    val migration_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE 'ORDER' ADD COLUMN is_completed INTEGER NOT NULL DEFAULT 0")
        }
    }
}