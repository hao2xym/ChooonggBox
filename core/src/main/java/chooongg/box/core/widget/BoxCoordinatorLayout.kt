package chooongg.box.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.coordinatorlayout.R
import androidx.coordinatorlayout.widget.CoordinatorLayout

class BoxCoordinatorLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.coordinatorLayoutStyle,
) : CoordinatorLayout(context, attrs, defStyleAttr) {

    private var onInterceptTouchListener: (() -> Unit)? = null

    fun setOnInterceptTouchListener(block: () -> Unit) {
        onInterceptTouchListener = block
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        onInterceptTouchListener?.invoke()
        return super.onInterceptTouchEvent(ev)
    }
}