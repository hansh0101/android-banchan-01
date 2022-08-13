package co.kr.woowahan_banchan.util

fun Int.toPriceFormat(): String = String.format("%,2d", this)

fun Long.calculateDiffToMinute(time: Long): Int = ((this - time) / 60000).toInt()