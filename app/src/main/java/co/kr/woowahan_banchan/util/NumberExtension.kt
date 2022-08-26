package co.kr.woowahan_banchan.util

import android.content.res.Resources

fun Int.toPriceFormat(): String = String.format("%,2d", this)
fun Int.dpToPx(): Int = this * Resources.getSystem().displayMetrics.density.toInt()
fun Long.calculateDiffToMinute(time: Long): Int = ((this - time) / 60000).toInt()
fun Long.calculateDiffToSecond(time: Long): Int = ((this - time) / 10000).toInt()
fun Long.calculateDiff(time: Long): String {
    val diffMinute = this.calculateDiffToMinute(time)
    if (diffMinute < 60) {
        return "${diffMinute}분 전"
    }
    val diffHour = diffMinute / 60
    if (diffHour < 24) {
        return "${diffHour}시간 전"
    }
    val diffDay = diffHour / 24
    if (diffDay < 30) {
        return "${diffDay}일 전"
    }
    val diffMonth = diffDay / 30
    if (diffMonth < 12) {
        return "${diffMonth}달 전"
    }
    val diffYear = diffMonth / 12
    return "${diffYear}년 전"
}