package co.kr.woowahan_banchan.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartDto(
    @ColumnInfo(name = "hash") @PrimaryKey(autoGenerate = false) val hash : String,
    @ColumnInfo(name = "amount") val amount : Int
)
