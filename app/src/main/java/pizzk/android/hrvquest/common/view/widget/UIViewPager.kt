package pizzk.android.hrvquest.common.view.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class UIViewPager(context: Context, attrs: AttributeSet?) : ViewPager(context, attrs) {
    private var scroll: Boolean = true

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return scroll && super.onInterceptTouchEvent(ev)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return scroll && super.onTouchEvent(ev)
    }

    /**
     * 禁止滑动控制
     */
    fun forbidScroll(value: Boolean = false) {
        scroll = value
    }
}