package co.kr.woowahan_banchan.data.model.local

import androidx.room.*
import androidx.room.ForeignKey.CASCADE
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderItem

@Entity(
    tableName = "ORDER_ITEM",
    foreignKeys = [
        ForeignKey(
            entity = OrderDto::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("order_id"),
            onDelete = CASCADE
        )
    ]
)
data class OrderItemDto(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "order_id") val orderId: Long,
    val hash: String,
    @ColumnInfo(name = "thumbnail_url") val thumbnailUrl: String,
    val price: Int,
    val amount: Int,
    val name: String
) {
    fun toOrderItem(): OrderItem {
        return OrderItem(
            hash,
            thumbnailUrl,
            name,
            amount,
            price
        )
    }
}