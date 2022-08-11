package co.kr.woowahan_banchan.domain.entity.detail

data class DishInfo(
    val topImageUrl: String,
    val thumbnailUrls: List<String>,
    val productDescription: String,
    val point: Int,
    val deliveryInfo: String,
    val deliveryFeeInfo: String,
    val prices: List<Int>,
    val detailImageUrls: List<String>
)