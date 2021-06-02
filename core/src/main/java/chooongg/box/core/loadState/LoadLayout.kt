package chooongg.box.core.loadState

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.NestedScrollingChild2
import androidx.core.view.NestedScrollingChildHelper
import chooongg.box.core.loadState.callback.Callback
import chooongg.box.core.loadState.callback.SuccessCallback
import chooongg.box.ext.isMainThread
import kotlin.collections.set
import kotlin.reflect.KClass

class LoadLayout : FrameLayout, NestedScrollingChild2 {

    companion object {
        private const val CALLBACK_CUSTOM_INDEX = 1
    }

    private var onReloadListener: ((KClass<out Callback>) -> Unit)? = null

    val callbacks = HashMap<KClass<out Callback>, Callback>()
    var preCallback: KClass<out Callback>? = null
        private set
    var currentCallback: KClass<out Callback>? = null
        private set

    var verticalPercentage = 0.5f
        internal set(value) {
            if (field != value) {
                field = value
                callbacks[currentCallback]?.setVerticalPercentage(value)
            }
        }
    var horizontalPercentage = 0.5f
        internal set(value) {
            if (field != value) {
                field = value
                callbacks[currentCallback]?.setHorizontalPercentage(value)
            }
        }

    private val nestedScrollingChildHelper = NestedScrollingChildHelper(this).apply {
        this.isNestedScrollingEnabled = true
    }

    constructor(context: Context) : super(context)

    constructor(
        context: Context,
        onReloadListener: ((KClass<out Callback>) -> Unit)?
    ) : super(context) {
        this.onReloadListener = onReloadListener
    }

    fun setupSuccessLayout(callback: Callback) {
        addCallback(callback)
        val successView = callback.getRootView()
        addView(
            successView, ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        currentCallback = SuccessCallback::class
    }

    fun setupCallback(callback: Callback) {
        callback.setCallback(context, onReloadListener)
        addCallback(callback)
    }

    fun addCallback(callback: Callback) {
        if (!callbacks.containsKey(callback::class)) {
            callbacks[callback::class] = callback
        }
    }

    fun showCallback(callback: KClass<out Callback>) {
        checkCallbackExist(callback)
        if (isMainThread()) showCallbackView(callback) else post { showCallbackView(callback) }
    }

    private fun showCallbackView(status: KClass<out Callback>) {
        if (preCallback != null) {
            if (preCallback == status) return
            callbacks[preCallback!!]!!.onDetach(context, callbacks[preCallback!!]!!.getRootView())
        }
        if (childCount > 1) removeViewAt(CALLBACK_CUSTOM_INDEX)
        callbacks.forEach {
            if (it.key == status) {
                val successCallback = callbacks[SuccessCallback::class] as SuccessCallback
                if (it.key == SuccessCallback::class) {
                    successCallback.show().apply {
                        if (successCallback.isEnableAnimation()) {
                            startAnimation(successCallback.getAnimation())
                        }
                    }
                } else {
                    successCallback.showWithCallback(callbacks[it.key]!!.successViewVisible)
                    val rootView = callbacks[it.key]!!.getRootView()
                    addView(rootView)
                    callbacks[it.key]!!.onAttach(context, rootView)
                    callbacks[it.key]!!.setVerticalPercentage(verticalPercentage)
                    callbacks[it.key]!!.setHorizontalPercentage(horizontalPercentage)
                    if (callbacks[it.key]!!.isEnableAnimation()) {
                        rootView.startAnimation(callbacks[it.key]!!.getAnimation())
                    }
                }
                preCallback = status
            }
        }
        currentCallback = status
    }

    fun setCallback(callback: KClass<out Callback>, transport: Transport) {
        checkCallbackExist(callback)
        transport.order(context, callbacks[callback]!!.obtainRootView())
    }

    private fun checkCallbackExist(callback: KClass<out Callback>) {
        if (!callbacks.containsKey(callback)) {
            throw IllegalArgumentException(
                String.format("The Callback (%s) is nonexistent.", callback.simpleName)
            )
        }
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