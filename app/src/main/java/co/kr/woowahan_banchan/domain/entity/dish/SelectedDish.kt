package co.kr.woowahan_banchan.domain.entity.dish

import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import kotlin.math.roundToInt

data class SelectedDish(
    val hash : String,
    val title : String,
    val imageUrl : String,
    val discount : Int,
    val nPrice : Int? = null,
    val sPrice : Int
){
    constructor(dish : Dish) : this (
        dish.detailHash,
        dish.title,
        dish.imageUrl,
        dish.discount,
        dish.nPrice,
        dish.sPrice
    )

    constructor(historyItem : HistoryItem) : this(
        historyItem.detailHash,
        historyItem.title,
        historyItem.imageUrl,
        if (historyItem.nPrice == null) {
            0
        } else {
            ((historyItem.nPrice - historyItem.sPrice).toDouble() / historyItem.nPrice.toDouble()).roundToInt()
        },
        historyItem.nPrice,
        historyItem.sPrice
    )

    val nPriceText: String
        get() =
            if (nPrice != null)
                "${String.format("%,2d", nPrice)}원"
            else
                ""

    val sPriceText: String
        get() = "${String.format("%,2d", sPrice)}원"
}
