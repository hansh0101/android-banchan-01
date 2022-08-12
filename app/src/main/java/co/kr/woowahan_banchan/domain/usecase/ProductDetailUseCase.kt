package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.repository.DetailRepository
import javax.inject.Inject

class ProductDetailUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(hash: String): Result<DishInfo> {
        return detailRepository.getDishInfo(hash)?.let {
            Result.success(it)
        } ?: Result.failure(IllegalStateException("상품을 불러오지 못했습니다"))
    }
}