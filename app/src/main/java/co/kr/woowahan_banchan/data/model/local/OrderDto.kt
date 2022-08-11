package co.kr.woowahan_banchan.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import co.kr.woowahan_banchan.domain.entity.orderhistory.OrderHistory

@Entity(tableName = "ORDER")
data class OrderDto(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    @ColumnInfo(name = "total_price") val totalPrice : Int,
    val time : Long
) {
    fun toOrderHistory(thumbnailUrl: String, title: String, count: Int): OrderHistory {
        return OrderHistory(
            this.id,
            thumbnailUrl,
            title,
            count,
            this.totalPrice,
            this.time
        )
    }
}
