package co.kr.woowahan_banchan.data.datasource.local.cart

import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow

interface CartDataSource {
    fun getItems(): Flow<List<CartDto>>
    suspend fun insertOrUpdateItems(items: List<CartDto>): Result<Unit>
    suspend fun deleteItems(ids: List<String>): Result<Unit>
}