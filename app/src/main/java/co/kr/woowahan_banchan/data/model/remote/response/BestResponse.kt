package co.kr.woowahan_banchan.data.model.remote.response

import com.google.gson.annotations.SerializedName

data class BestResponse(
    @SerializedName("category_id") val categoryId : String,
    val name : String,
    val items : List<DishResponse>
)
