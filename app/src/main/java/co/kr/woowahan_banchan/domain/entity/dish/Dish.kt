package co.kr.woowahan_banchan.domain.entity.dish

data class Dish(
    val detailHash: String,
    val imageUrl: String,
    val alt: String,
    val deliveryType: List<String>,
    val title: String,
    val description: String,
    val discount: Int,
    val nPrice: Int? = null,
    val sPrice: Int,
    val badge: List<String>? = null,
    val isAdded: Boolean
)