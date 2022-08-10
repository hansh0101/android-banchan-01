package co.kr.woowahan_banchan.data.datasource.remote.sidedish

import co.kr.woowahan_banchan.data.model.remote.response.DishResponse

interface SideDishDataSource {
    suspend fun getSideDishes() : Result<List<DishResponse>>
}