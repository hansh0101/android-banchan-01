package co.kr.woowahan_banchan.data.model.remote.response

import co.kr.woowahan_banchan.domain.entity.dish.Dish
import com.google.gson.annotations.SerializedName

data class DishResponse(
    @SerializedName("detail_hash") val detailHash: String,
    @SerializedName("image") val imageUrl: String,
    val alt: String,
    @SerializedName("delivery_type") val deliveryType: List<String>,
    val title: String,
    val description: String,
    @SerializedName("n_price") val nPrice: String? = null,
    @SerializedName("s_price") val sPrice: String,
    val badge: List<String>? = null
) {
    fun toEntity(isAdded: Boolean): Dish {
        val discount =
            if (nPrice == null) 0
            else ((nPrice.toPriceInt()!! - sPrice.toPriceInt()!!).toDouble() / nPrice.toPriceInt()!!
                .toDouble() * 100).toInt()

        return Dish(
            this.detailHash,
            this.imageUrl,
            this.alt,
            this.deliveryType,
            this.title,
            this.description,
            discount,
            nPrice.toPriceInt(),
            sPrice.toPriceInt() ?: 0,
            this.badge,
            isAdded
        )
    }

    private fun String?.toPriceInt(): Int? {
        return this?.replace("원", "")?.replace(",", "")?.toInt()
    }
}