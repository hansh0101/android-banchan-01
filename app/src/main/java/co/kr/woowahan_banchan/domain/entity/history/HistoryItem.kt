package co.kr.woowahan_banchan.domain.entity.history

import co.kr.woowahan_banchan.domain.entity.dish.Dish
import kotlin.math.roundToInt

data class HistoryItem(
    val detailHash: String,
    val imageUrl: String,
    val title: String,
    val nPrice: Int?,
    val sPrice: Int,
    val time: Long,
    val isAdded: Boolean
) {
    fun toDish(): Dish {
        return Dish(
            detailHash = detailHash,
            imageUrl = imageUrl,
            alt = this.title,
            deliveryType = listOf(),
            title = this.title,
            description = "",
            discount = if (nPrice == null) {
                0
            } else {
                ((nPrice - sPrice).toDouble() / nPrice.toDouble()).roundToInt()
            },
            nPrice = this.nPrice,
            sPrice = this.sPrice,
            badge = null,
            isAdded = this.isAdded
        )
    }
}