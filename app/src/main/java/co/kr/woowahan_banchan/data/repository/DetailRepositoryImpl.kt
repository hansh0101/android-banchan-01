package co.kr.woowahan_banchan.data.repository

import co.kr.woowahan_banchan.data.datasource.remote.detail.DetailDataSource
import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.repository.DetailRepository
import javax.inject.Inject

class DetailRepositoryImpl @Inject constructor(
    private val detailDataSource: DetailDataSource
) : DetailRepository {
    override suspend fun getDishInfo(hash: String): DishInfo? {
        val apiResult = detailDataSource.getDetail(hash)
        return when (apiResult.isSuccess) {
            true -> {
                apiResult.getOrNull()?.data?.toDishInfo()
            }
            false -> null
        }
    }
}