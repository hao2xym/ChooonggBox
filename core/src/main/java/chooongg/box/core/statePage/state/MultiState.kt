package chooongg.box.core.statePage.state

import android.content.Context
import android.view.View
import chooongg.box.core.statePage.StatePageLayout

abstract class MultiState {

    /**
     * 创建stateView
     */
    abstract fun onCreateMultiStateView(
        context: Context,
        container: StatePageLayout
    ): View

    /**
     * stateView创建完成
     */
    abstract fun onMultiStateViewCreate(view: View)

    /**
     * 设置垂直百分比偏移
     */
    open fun setVerticalPercentage(percentage: Float) = Unit

    /**
     * 设置水平百分比偏移
     */
    open fun setHorizontalPercentage(percentage: Float) = Unit

    /**
     * 是否显示成功视图
     */
    open fun isShowSuccessView() = false

    /**
     * 是否允许重新加载 点击事件
     * 默认允许
     */
    open fun enableReload(): Boolean = true

    /**
     * 绑定重试view
     * 默认null为整个state view
     */
    open fun bindRetryView(): View? = null

    abstract fun setText(text: CharSequence)
    abstract fun getText(): CharSequence

}