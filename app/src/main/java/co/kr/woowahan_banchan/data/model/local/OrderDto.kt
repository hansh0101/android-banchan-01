package co.kr.woowahan_banchan.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OrderDto(
    @ColumnInfo(name = "id") @PrimaryKey(autoGenerate = true) val id : Int = 0,
    @ColumnInfo(name = "total_price") val totalPrice : Int,
    @ColumnInfo(name = "time") val time : Long
)
