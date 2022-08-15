package co.kr.woowahan_banchan.presentation.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val firstHeight: Int,
    private val height : Int,
    private val width : Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = (view.layoutParams as GridLayoutManager.LayoutParams).spanIndex
        val position = parent.getChildLayoutPosition(view)

        if (index == 0) {
            outRect.left = width
            outRect.right = width / 2
        }else {
            outRect.left = width / 2
            outRect.right = width
        }

        if (position < 2) {
            outRect.top = firstHeight
        } else {
            outRect.top = height
        }
    }
}