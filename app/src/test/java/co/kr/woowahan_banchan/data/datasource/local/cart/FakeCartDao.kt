package co.kr.woowahan_banchan.data.datasource.local.cart

import co.kr.woowahan_banchan.data.database.dao.CartDao
import co.kr.woowahan_banchan.data.model.local.CartDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeCartDao(
    initCartDtos: List<CartDto> = listOf()
) : CartDao {
    val cartDtos = initCartDtos.toMutableList()

    override fun getItems(): Flow<List<CartDto>> {
        return flow { emit(cartDtos) }
    }

    override suspend fun insertOrUpdateItems(items: List<CartDto>) {
        items.forEach { item ->
            when (val findResult = cartDtos.find { it.hash == item.hash }) {
                null -> cartDtos.add(item)
                else -> cartDtos[cartDtos.indexOf(findResult)] = item
            }
        }
    }

    override suspend fun deleteItems(ids: List<String>) {
        ids.forEach { id ->
            cartDtos.removeIf { it.hash == id }
        }
    }

    override suspend fun getAmount(hash: String): Int {
        return cartDtos.find { it.hash == hash }?.amount ?: 0
    }
}
