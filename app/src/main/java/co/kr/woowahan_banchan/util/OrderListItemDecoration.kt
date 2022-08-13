package co.kr.woowahan_banchan.util

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class OrderListItemDecoration(
    private val topOffset: Int,
    private val bottomOffset: Int,
    private val borderWidth: Int,
    private val borderColor: Int
) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.layoutManager is LinearLayoutManager) {
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.set(
                    0, (topOffset + bottomOffset).dpToPx(), 0, bottomOffset.dpToPx()
                )
            } else if (parent.getChildAdapterPosition(view) == parent.childCount - 1) {
                outRect.set(0, topOffset.dpToPx(), 0, (topOffset + bottomOffset).dpToPx())
            } else {
                outRect.set(0, topOffset.dpToPx(), 0, bottomOffset.dpToPx())
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val paint = Paint().apply {
            this.color = borderColor
            this.style = Paint.Style.STROKE
            this.strokeWidth = borderWidth.dpToPx().toFloat()
        }

        parent.children.forEach { child ->
            c.drawRect(
                child.left.toFloat(),
                child.top.toFloat(),
                child.right.toFloat(),
                (child.top + (1).dpToPx()).toFloat(),
                paint
            )

            c.drawRect(
                child.left.toFloat(),
                (child.bottom - (1).dpToPx()).toFloat(),
                child.right.toFloat(),
                child.bottom.toFloat(),
                paint
            )
        }
    }
}