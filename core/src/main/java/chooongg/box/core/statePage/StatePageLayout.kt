package chooongg.box.core.statePage

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import chooongg.box.core.statePage.state.MultiState
import chooongg.box.core.statePage.state.SuccessState
import chooongg.box.ext.inVisible
import chooongg.box.ext.resourcesInteger
import chooongg.box.ext.visible
import kotlin.reflect.KClass

class StatePageLayout : FrameLayout, NestedScrollingChild2 {

    private var onRetryEventListener: ((MultiState) -> Unit)? = null

    private var onStateChangeListener: ((MultiState) -> Unit)? = null

    private var originTargetView: View? = null

    private var statePool: MutableMap<KClass<out MultiState>, MultiState> =
        mutableMapOf(Pair(SuccessState::class, SuccessState()))

    private var lastState: MultiState? = null

    private var enableAnimation = StatePage.config.enableAnimation

    var verticalPercentage = 0.5f
        internal set(value) {
            if (field != value) {
                field = value
                lastState?.setVerticalPercentage(value)
            }
        }
    var horizontalPercentage = 0.5f
        internal set(value) {
            if (field != value) {
                field = value
                lastState?.setHorizontalPercentage(value)
            }
        }

    private val nestedScrollingChildHelper = NestedScrollingChildHelper(this).apply {
        this.isNestedScrollingEnabled = true
    }

    constructor(
        context: Context,
        originTargetView: View,
        onRetryEventListener: ((MultiState) -> Unit)? = null
    ) : super(context) {
        this.originTargetView = originTargetView
        this.onRetryEventListener = onRetryEventListener
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        if (originTargetView == null && childCount > 0) {
            originTargetView = getChildAt(0)
        }
        show(StatePage.config.defaultState)
    }

    fun <T : MultiState> show(multiState: T, text: CharSequence? = null) {
        if (childCount == 0) {
            initialization()
        }
        if (lastState != null && lastState == multiState) {
            return
        }
        if (childCount > 1) {
            removeViewAt(1)
        }
        if (multiState is SuccessState) {
            //如果上次展示的是SuccessState则跳过
            if (lastState != SuccessState::class) {
                originTargetView?.visible()
                if (enableAnimation && lastState != null && !lastState!!.isShowSuccessView()) {
                    originTargetView?.startAnimation()
                }
                onStateChangeListener?.invoke(multiState)
            }
        } else {
            if (multiState.isShowSuccessView()) {
                originTargetView?.visible()
            } else {
                originTargetView?.inVisible()
            }
            val currentStateView = multiState.onCreateMultiStateView(context, this)
            multiState.onMultiStateViewCreate(currentStateView)
            multiState.setVerticalPercentage(verticalPercentage)
            multiState.setHorizontalPercentage(horizontalPercentage)
            if (text != null) multiState.setText(text)
            val retryView = multiState.bindRetryView()
            if (multiState.enableReload()) {
                if (retryView != null) {
                    retryView.setOnClickListener { onRetryEventListener?.invoke(multiState) }
                } else {
                    currentStateView.setOnClickListener { onRetryEventListener?.invoke(multiState) }
                }
            }
            super.addView(currentStateView)
            if (enableAnimation) currentStateView.startAnimation()
        }
        //记录上次展示的state
        lastState = multiState
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : MultiState> show(clazz: KClass<T>, text: CharSequence? = null) {
        findState(clazz)?.let { multiState ->
            show(multiState as T, text)
        }
    }

    fun showSuccess() {
        show(SuccessState::class)
    }

    private fun <T : MultiState> findState(clazz: KClass<T>): MultiState? {
        return if (statePool.containsKey(clazz)) {
            statePool[clazz]
        } else {
            val state = clazz.java.newInstance()
            statePool[clazz] = state
            state
        }
    }

    private fun View.startAnimation() {
        val animation = StatePage.config.animation ?: AlphaAnimation(0f, 1f).apply {
            duration = context.resourcesInteger(android.R.integer.config_mediumAnimTime).toLong()
        }
        startAnimation(animation)
    }

    fun setOnRetryEventListener(block: (MultiState) -> Unit) {
        onRetryEventListener = block
    }

    fun setOnStateChangeListener(block: (MultiState) -> Unit) {
        onStateChangeListener = block
    }

    internal fun initialization() {
        val layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(originTargetView, 0, layoutParams)
    }

    override fun startNestedScroll(axes: Int, type: Int): Boolean {
        return nestedScrollingChildHelper.startNestedScroll(axes, type)
    }

    override fun stopNestedScroll(type: Int) {
        nestedScrollingChildHelper.stopNestedScroll(type)
    }

    override fun hasNestedScrollingParent(type: Int): Boolean {
        return nestedScrollingChildHelper.hasNestedScrollingParent(type)
    }

    override fun dispatchNestedScroll(
        dxConsumed: Int,
        dyConsumed: Int,
        dxUnconsumed: Int,
        dyUnconsumed: Int,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return nestedScrollingChildHelper.dispatchNestedScroll(
            dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow, type
        )
    }

    override fun dispatchNestedPreScroll(
        dx: Int,
        dy: Int,
        consumed: IntArray?,
        offsetInWindow: IntArray?,
        type: Int
    ): Boolean {
        return nestedScrollingChildHelper.dispatchNestedPreScroll(
            dx, dy, consumed, offsetInWindow, type
        )
    }
}