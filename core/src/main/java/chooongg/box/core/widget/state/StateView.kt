package chooongg.box.core.widget.state

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.annotation.Keep
import chooongg.box.ext.doOnClick
import chooongg.box.ext.dp2px

@Keep
abstract class StateView {

    private lateinit var context: Context

    constructor()

    internal constructor(contentView: View) {
        rootView = contentView
    }

    protected lateinit var rootView: View

    protected var onStateEventListener: StateLayout.OnStateEventListener? = null

    /**
     * 构建状态视图
     */
    protected abstract fun onBuildView(context: Context): View

    /**
     * 点击事件视图
     */
    open fun onClickEventView(): View? = rootView

    open fun onText(): TextView? = null

    /**
     * 显示时调用
     */
    abstract fun show()

    /**
     * 隐藏时调用
     */
    abstract fun hide()

    /**
     * 设置 X 轴绝对偏移
     */
    open fun setAbsoluteOffsetX() = Unit

    /**
     * 设置 Y 轴绝对偏移
     */
    open fun setAbsoluteOffsetY() = Unit

    /**
     * 设置 X 轴相对偏移
     */
    open fun setPercentageOffsetX() = Unit

    /**
     * 设置 Y 轴相对偏移
     */
    open fun setPercentageOffsetY() = Unit

    /**
     * 获取横向内边距
     */
    open fun getPaddingVertical() = dp2px(16f)

    /**
     * 获取纵向内边距
     */
    open fun getPaddingHorizontal() = dp2px(16f)

    /**
     * 是否启用动画过渡 null 为全局设置
     */
    open fun isEnableAnimation(): Boolean? = null

    internal fun getRootView(context: Context): View {
        rootView = onBuildView(context)
        onClickEventView()?.doOnClick {
            onStateEventListener?.event(this::class, it)
        }
        return rootView
    }


    fun set(context: Context, onStateEventListener: StateLayout.OnStateEventListener?) = apply {
        this.context = context
        this.onStateEventListener = onStateEventListener
    }
}