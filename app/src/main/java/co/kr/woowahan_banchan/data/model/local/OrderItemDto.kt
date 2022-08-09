package co.kr.woowahan_banchan.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderItemDto(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id : Int,
    @ColumnInfo(name = "order_id") val orderId : Int,
    @ColumnInfo(name = "thumbnail") val thumbnailUrl : String,
    @ColumnInfo(name = "price") val price : Int,
    @ColumnInfo(name = "amount") val amount : Int,
    @ColumnInfo(name = "name") val name : String
)
