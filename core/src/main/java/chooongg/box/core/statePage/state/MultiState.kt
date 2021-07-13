package chooongg.box.core.statePage.state

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import chooongg.box.core.R
import chooongg.box.core.statePage.StatePageLayout
import chooongg.box.ext.APP
import chooongg.box.ext.resourcesInteger

abstract class MultiState {

    private lateinit var view: View

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

    open fun showAnimation(): Animation? = AnimationSet(true).apply {
        duration = APP.resourcesInteger(R.integer.material_motion_duration_medium_1).toLong()
        interpolator = LinearOutSlowInInterpolator()
        addAnimation(AlphaAnimation(0f, 1f))
        addAnimation(
            ScaleAnimation(
                0.8f, 1f,
                0.8f, 1f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            )
        )
    }

    abstract fun setText(text: CharSequence)
    abstract fun getText(): CharSequence

}