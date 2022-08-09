package co.kr.woowahan_banchan.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class DishResponse(
    @SerializedName("detail_hash") val detailHash : String,
    @SerializedName("image") val imageUrl : String,
    val alt : String,
    @SerializedName("delivery_type")val deliveryType : List<String>,
    val title : String,
    val description : String,
    @SerializedName("n_price") val nPrice : String? = null,
    @SerializedName("s_price") val sPrice : String,
    val badge : List<String>? = null
)