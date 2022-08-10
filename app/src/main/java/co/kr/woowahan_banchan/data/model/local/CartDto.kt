package co.kr.woowahan_banchan.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "CART")
data class CartDto(
    @PrimaryKey(autoGenerate = false) val hash: String,
    val amount: Int,
    @ColumnInfo(name = "is_selected") val isSelected: Boolean,
    val time: Long,
    val name: String
)