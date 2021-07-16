package chooongg.box.core.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.OverScroller
import androidx.coordinatorlayout.widget.CoordinatorLayout
import chooongg.box.core.R
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.AppBarLayout.OnOffsetChangedListener
import java.lang.reflect.Field
import kotlin.math.abs

class BoxAppBarLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.appBarLayoutStyle,
) : AppBarLayout(context, attrs, defStyleAttr) {

    enum class State {
        EXPANDED, COLLAPSED, INTERMEDIATE
    }

    private var state: State = State.EXPANDED

    fun addOnStateChangedListener(block: (appBarLayout: AppBarLayout, verticalOffset: Int, state: State) -> Unit) {
        addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (state != State.EXPANDED) {
                    state = State.EXPANDED
                    block(appBarLayout, verticalOffset, state)
                }
            } else if (abs(verticalOffset) >= totalScrollRange) {
                if (state != State.COLLAPSED) {
                    state = State.COLLAPSED
                    block(appBarLayout, verticalOffset, state)
                }
            } else {
                if (state != State.INTERMEDIATE) {
                    state = State.INTERMEDIATE
                    block(appBarLayout, verticalOffset, state)
                }
            }
        })
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<AppBarLayout> {
        return Behavior()
    }

    class Behavior : AppBarLayout.Behavior {

        constructor() : super()
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

        override fun onInterceptTouchEvent(
            parent: CoordinatorLayout,
            child: AppBarLayout,
            ev: MotionEvent
        ): Boolean {
            if (ev.action == MotionEvent.ACTION_DOWN) {
                val scroller = getSuperSuperField(this, "mScroller")
                if (scroller != null && scroller is OverScroller) {
                    scroller.abortAnimation()
                }
            }
            return super.onInterceptTouchEvent(parent, child, ev)
        }

        private fun getSuperSuperField(paramClass: Any, paramString: String): Any? {
            val field: Field?
            var any: Any? = null
            try {
                field = paramClass.javaClass.superclass.superclass.getDeclaredField(paramString)
                field.isAccessible = true
                any = field.get(paramClass)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return any
        }
    }
}