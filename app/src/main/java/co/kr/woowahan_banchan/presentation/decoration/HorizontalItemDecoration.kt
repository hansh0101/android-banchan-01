package co.kr.woowahan_banchan.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalItemDecoration(
    private val width: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        when(parent.getChildLayoutPosition(view)){
            0 -> {
                outRect.left = width
                outRect.right = width
            }
            else -> {
                outRect.right = width
            }
        }
    }
}