package co.kr.woowahan_banchan.util

import android.content.res.Resources

fun Int.toPriceFormat(): String = String.format("%,2d", this)
fun Int.dpToPx(): Int = this * Resources.getSystem().displayMetrics.density.toInt()
fun Long.calculateDiffToMinute(time: Long): Int = ((this - time) / 60000).toInt()