package co.kr.woowahan_banchan.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM CART")
    fun getItems(): Flow<List<CartDto>>

    @Insert(onConflict = REPLACE)
    suspend fun insertOrUpdateItems(items: List<CartDto>)

    @Query("DELETE FROM CART WHERE hash in (:ids)")
    suspend fun deleteItems(ids: List<String>)

    @Query("SELECT amount FROM CART WHERE hash LIKE (:hash)")
    suspend fun getAmount(hash: String): Int
}
