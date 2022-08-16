package co.kr.woowahan_banchan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.kr.woowahan_banchan.data.model.local.OrderDto
import kotlinx.coroutines.flow.Flow

@Dao
interface OrderDao {
    @Query("SELECT * FROM `ORDER` ORDER BY id DESC")
    suspend fun getItems(): List<OrderDto>

    @Query("SELECT time FROM `ORDER` ORDER BY id DESC LIMIT 1")
    fun getLatestOrderTime(): Flow<Long>

    @Query("SELECT time FROM `ORDER` WHERE id LIKE (:orderId)")
    suspend fun getTime(orderId: Long): Long

    @Insert
    suspend fun insertItem(item: OrderDto): Long
}