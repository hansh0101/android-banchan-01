package co.kr.woowahan_banchan.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ORDER")
data class OrderDto(
    @PrimaryKey(autoGenerate = true) val id : Long = 0,
    @ColumnInfo(name = "total_price") val totalPrice : Int,
    val time : Long
)
