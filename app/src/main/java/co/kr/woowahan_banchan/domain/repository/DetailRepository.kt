package co.kr.woowahan_banchan.domain.repository

import co.kr.woowahan_banchan.domain.entity.detail.DishInfo

interface DetailRepository {
    suspend fun getDishInfo(hash: String): DishInfo?
}