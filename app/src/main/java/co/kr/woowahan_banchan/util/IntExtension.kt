package co.kr.woowahan_banchan.util

fun Int.toPriceFormat(): String {
    return String.format("%,2d", this)
}