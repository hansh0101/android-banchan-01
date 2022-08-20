package co.kr.woowahan_banchan.domain.entity.history

import co.kr.woowahan_banchan.domain.entity.dish.SelectedDish

data class HistoryItem(
    val detailHash: String,
    val imageUrl: String,
    val title: String,
    val nPrice: Int?,
    val sPrice: Int,
    val time: Long,
    val isAdded: Boolean
){
    fun toSelectedDish() : SelectedDish = SelectedDish(this)
}