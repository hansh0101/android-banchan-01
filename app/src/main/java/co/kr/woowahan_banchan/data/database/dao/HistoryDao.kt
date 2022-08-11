package co.kr.woowahan_banchan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import co.kr.woowahan_banchan.data.model.local.HistoryDto
import kotlinx.coroutines.flow.Flow

@Dao
interface HistoryDao {
    @Query("SELECT * FROM HISTORY ORDER BY time DESC")
    fun getItems(): Flow<List<HistoryDto>>

    @Query("SELECT * FROM HISTORY ORDER BY time DESC LIMIT 7")
    fun getPreviewItems(): Flow<List<HistoryDto>>

    @Insert(onConflict = REPLACE)
    suspend fun insertItem(item: HistoryDto)
}