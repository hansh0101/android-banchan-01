package co.kr.woowahan_banchan.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class DetailDataResponse(
    @SerializedName("top_image") val topImageUrl : String,
    @SerializedName("thumb_images") val thumbnailUrls : List<String>,
    @SerializedName("product_description") val productDescription : String,
    val point : String,
    @SerializedName("delivery_info") val deliveryInfo : String,
    @SerializedName("delivery_fee") val deliveryFee : String,
    val prices : List<String>,
    @SerializedName("detail_section") val detailSection : List<String>
)
