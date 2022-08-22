package co.kr.woowahan_banchan.domain.usecase

import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.repository.DetailRepository
import javax.inject.Inject

class ProductDetailUseCase @Inject constructor(
    private val detailRepository: DetailRepository
) {
    suspend operator fun invoke(hash: String): Result<DishInfo> =
        detailRepository.getDishInfo(hash)
}