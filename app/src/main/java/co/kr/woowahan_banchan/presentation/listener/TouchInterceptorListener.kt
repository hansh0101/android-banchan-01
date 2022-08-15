package co.kr.woowahan_banchan.presentation.listener

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class TouchInterceptorListener(
    context: Context,
    parent: ViewParent
) : RecyclerView.OnItemTouchListener {
    private val gestureDetector = GestureDetector(context, GestureListener(parent))

    override fun onInterceptTouchEvent(recyclerView: RecyclerView, e: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(e)
        return false
    }

    override fun onTouchEvent(recyclerView: RecyclerView, e: MotionEvent) {}

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {}

    private class GestureListener(
        private val parent: ViewParent
    ) :
        GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent): Boolean {
            parent.requestDisallowInterceptTouchEvent(true)
            return super.onDown(e)
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            if (abs(distanceX) < abs(distanceY)) {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            return super.onScroll(e1, e2, distanceX, distanceY)
        }
    }
}
