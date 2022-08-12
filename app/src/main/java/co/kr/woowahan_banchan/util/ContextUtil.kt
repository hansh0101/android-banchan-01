package co.kr.woowahan_banchan.util

import android.content.Context
import android.widget.Toast

fun Context.shortToast(message: String?) {
    Toast.makeText(this, message ?: "문제가 발생했습니다", Toast.LENGTH_SHORT).show()
}