package co.kr.woowahan_banchan.data.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "HISTORY")
data class HistoryDto(
    @PrimaryKey(autoGenerate = false) val hash: String,
    val time: Long,
    val name: String
)
