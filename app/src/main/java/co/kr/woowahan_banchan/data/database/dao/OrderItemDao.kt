package co.kr.woowahan_banchan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.kr.woowahan_banchan.data.model.local.OrderItemDto
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM ORDER_ITEM")
    fun getItems(): Flow<List<OrderItemDto>>

    @Insert
    fun insertItems(items: List<OrderItemDto>)
}