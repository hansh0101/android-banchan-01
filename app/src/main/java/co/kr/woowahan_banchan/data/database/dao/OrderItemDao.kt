package co.kr.woowahan_banchan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import co.kr.woowahan_banchan.data.model.local.OrderItemDto

@Dao
interface OrderItemDao {
    @Query("SELECT * FROM ORDER_ITEM WHERE order_id LIKE (:orderId)")
    suspend fun getItems(orderId: Long): List<OrderItemDto>

    @Query("SELECT * FROM ORDER_ITEM WHERE order_id LIKE (:orderId) LIMIT 1")
    suspend fun getItem(orderId: Long): OrderItemDto

    @Query("SELECT COUNT(id) FROM ORDER_ITEM WHERE order_id LIKE (:orderId)")
    suspend fun getItemCount(orderId: Long): Int

    @Insert
    suspend fun insertItems(items: List<OrderItemDto>)
}