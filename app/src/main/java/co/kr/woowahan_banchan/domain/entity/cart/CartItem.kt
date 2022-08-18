package co.kr.woowahan_banchan.domain.entity.cart

data class CartItem(
    val hash : String,
    val name : String,
    var isSelected: Boolean,
    var amount : Int,
    val imageUrl : String,
    val price : Int
){
    val priceText : String
        get() = "${String.format("%,2d", price)}원"

    val totalPriceText : String
        get() = "${String.format("%,2d", amount*price)}원"
}
