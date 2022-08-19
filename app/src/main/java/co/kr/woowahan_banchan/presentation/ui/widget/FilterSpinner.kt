package co.kr.woowahan_banchan.presentation.ui.widget

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatSpinner

class FilterSpinner(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatSpinner(context, attrs) {

    private var isOpened = false
    private var dropdownStart: () -> Unit = { }
    private var dropdownEnd: () -> Unit = { }

    override fun performClick(): Boolean {
        isOpened = true
        dropdownStart()
        return super.performClick()
    }

    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        if (isOpened && hasWindowFocus) {
            isOpened = false
            dropdownEnd()
        }
        super.onWindowFocusChanged(hasWindowFocus)
    }

    fun setDropDownStartEvent(event: () -> Unit) {
        dropdownStart = event
    }

    fun setDropDownEndEvent(event: () -> Unit) {
        dropdownEnd = event
    }
}