package co.kr.woowahan_banchan.domain.entity.dish

import java.text.NumberFormat

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
) {
    val nPriceText: String
        get() =
            if (nPrice != null)
                "${NumberFormat.getNumberInstance().format(nPrice)}원"
            else
                ""

    val sPriceText: String
        get() = "${NumberFormat.getNumberInstance().format(sPrice)}원"
}