package co.kr.woowahan_banchan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM `ORDER`")
    fun getItems(): Flow<List<OrderDto>>

    @Insert
    suspend fun insertItem(item: OrderDto): Long
}