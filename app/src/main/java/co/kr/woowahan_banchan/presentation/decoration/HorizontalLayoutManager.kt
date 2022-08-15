package co.kr.woowahan_banchan.presentation.decoration

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HorizontalLayoutManager(
    context: Context,
    orientation: Int = RecyclerView.HORIZONTAL,
    reverseLayout: Boolean = false,
    private val ratio: Float
) : LinearLayoutManager(context, orientation, reverseLayout) {

    private val horizontalSpace
        get() = width - paddingStart - paddingEnd

    override fun generateDefaultLayoutParams() =
        horizontalLayoutParams(super.generateDefaultLayoutParams())

    override fun generateLayoutParams(layoutParams: ViewGroup.LayoutParams?) =
        horizontalLayoutParams(super.generateLayoutParams(layoutParams))

    override fun generateLayoutParams(context: Context?, attrs: AttributeSet?) =
        horizontalLayoutParams(super.generateLayoutParams(context, attrs))

    private fun horizontalLayoutParams(layoutParams: RecyclerView.LayoutParams) =
        layoutParams.apply {
            width = (horizontalSpace * ratio).toInt()
        }
}