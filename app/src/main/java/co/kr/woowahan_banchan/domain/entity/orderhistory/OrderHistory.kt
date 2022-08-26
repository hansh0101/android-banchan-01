package co.kr.woowahan_banchan.domain.entity.orderhistory

data class OrderHistory(
    val orderId: Long,
    val thumbnailUrl: String,
    val title: String,
    val count: Int,
    val totalPrice: Int,
    val time: Long,
    val isCompleted: Boolean
)
