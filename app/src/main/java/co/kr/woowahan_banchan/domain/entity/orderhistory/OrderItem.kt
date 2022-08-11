package co.kr.woowahan_banchan.domain.entity.orderhistory

data class OrderItem(
    val hash: String,
    val thumbnailUrl: String,
    val title: String,
    val amount: Int,
    val price: Int
)
