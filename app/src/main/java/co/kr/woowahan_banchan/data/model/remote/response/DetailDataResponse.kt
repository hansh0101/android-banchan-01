package co.kr.woowahan_banchan.data.model.remote.response

import co.kr.woowahan_banchan.domain.entity.cart.CartItem
import co.kr.woowahan_banchan.domain.entity.detail.DishInfo
import co.kr.woowahan_banchan.domain.entity.history.HistoryItem
import com.google.gson.annotations.SerializedName
import kotlin.math.roundToInt

data class DetailDataResponse(
    @SerializedName("top_image") val topImageUrl: String,
    @SerializedName("thumb_images") val thumbnailUrls: List<String>,
    @SerializedName("product_description") val productDescription: String,
    val point: String,
    @SerializedName("delivery_info") val deliveryInfo: String,
    @SerializedName("delivery_fee") val deliveryFee: String,
    val prices: List<String>,
    @SerializedName("detail_section") val detailSection: List<String>
) {
    fun toHistoryItem(hash: String, title: String, time: Long, isAdded: Boolean): HistoryItem {
        return HistoryItem(
            hash,
            this.thumbnailUrls.first(),
            title,
            if (prices.size == 1) null else prices.first().toPriceInt(),
            if (prices.size == 1) prices.first().toPriceInt() else prices.last().toPriceInt(),
            time,
            isAdded
        )
    }

    fun toDishInfo(): DishInfo {
        return DishInfo(
            topImageUrl,
            thumbnailUrls,
            productDescription,
            point.toPriceInt(),
            deliveryInfo,
            deliveryFee,
            prices.map {
                it.toPriceInt()
            },
            if (prices.size == 1) 0 else ((prices[0].toPriceInt() - prices[1].toPriceInt()).toDouble() / (prices[0].toPriceInt()).toDouble() * 100).roundToInt(),
            detailSection
        )
    }

    fun toCartItem(hash: String, name : String, isSelected: Boolean,amount:Int) : CartItem {
        return CartItem(
            hash,
            name,
            isSelected,
            amount,
            this.thumbnailUrls.first(),
            prices.last().toPriceInt()
        )
    }

    private fun String.toPriceInt(): Int {
        return this.replace("원", "").replace(",", "").toInt()
    }
}