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
                    0, topOffset + bottomOffset, 0, bottomOffset
                )
            } else if (parent.getChildAdapterPosition(view) == parent.childCount - 1) {
                outRect.set(0, topOffset, 0, (topOffset + bottomOffset))
            } else {
                outRect.set(0, topOffset, 0, bottomOffset)
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val paint = Paint().apply {
            this.color = borderColor
            this.style = Paint.Style.STROKE
            this.strokeWidth = borderWidth.toFloat()
        }

        parent.children.forEach { child ->
            c.drawRect(
                child.left.toFloat(),
                child.top.toFloat(),
                child.right.toFloat(),
                (child.top + 1).toFloat(),
                paint
            )

            c.drawRect(
                child.left.toFloat(),
                (child.bottom - 1).toFloat(),
                child.right.toFloat(),
                child.bottom.toFloat(),
                paint
            )
        }
    }
}