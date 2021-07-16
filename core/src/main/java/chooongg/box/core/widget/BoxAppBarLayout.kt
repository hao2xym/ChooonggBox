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

    private val behavior = Behavior()

    fun addOnStateChangedListener(block: (appBarLayout: AppBarLayout, state: State) -> Unit) {
        addOnOffsetChangedListener(OnOffsetChangedListener { appBarLayout, verticalOffset ->
            if (verticalOffset == 0) {
                if (state != State.EXPANDED) {
                    state = State.EXPANDED
                    block(appBarLayout, state)
                }
            } else if (abs(verticalOffset) >= totalScrollRange) {
                if (state != State.COLLAPSED) {
                    state = State.COLLAPSED
                    block(appBarLayout, state)
                }
            } else {
                if (state != State.INTERMEDIATE) {
                    state = State.INTERMEDIATE
                    block(appBarLayout, state)
                }
            }
        })
    }

    override fun getBehavior(): CoordinatorLayout.Behavior<AppBarLayout> {
        return behavior
    }

    class Behavior : AppBarLayout.Behavior {

        private var overScroller: OverScroller? = null

        constructor() : super()
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

        override fun onInterceptTouchEvent(
            parent: CoordinatorLayout,
            child: AppBarLayout,
            ev: MotionEvent
        ): Boolean {
            if (ev.action == MotionEvent.ACTION_UP) {
                reflectOverScroller()
            }
            return super.onInterceptTouchEvent(parent, child, ev)
        }

        fun stopFling() {
            overScroller?.abortAnimation()
        }

        private fun reflectOverScroller() {
            if (overScroller == null) {
                try {
                    val field: Field = javaClass.superclass.superclass.getDeclaredField("mScroller")
                    field.isAccessible = true
                    overScroller = field.get(this) as OverScroller
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}