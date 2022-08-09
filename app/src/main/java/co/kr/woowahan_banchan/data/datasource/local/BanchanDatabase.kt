package co.kr.woowahan_banchan.data.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import co.kr.woowahan_banchan.data.model.local.CartDto
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import co.kr.woowahan_banchan.data.model.local.OrderDto
import co.kr.woowahan_banchan.data.model.local.OrderItemDto

@Database(entities = [CartDto::class, HistoryDto::class, OrderDto::class, OrderItemDto::class], version = 1)
abstract class BanchanDatabase : RoomDatabase() {

}
