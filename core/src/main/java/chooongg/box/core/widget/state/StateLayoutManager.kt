package chooongg.box.core.widget.state

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.annotation.IntRange
import chooongg.box.ext.APP
import chooongg.box.ext.resourcesInteger

/**
 * 状态切换布局管理器
 */
object StateLayoutManager {

    internal var config = StateConfig()

    fun initialize(block: (StateConfig) -> StateConfig) {
        config = block.invoke(StateConfig())
    }

    /**
     * 全局配置类
     */
    class StateConfig {
        /**
         * 是否启用动画过渡
         */
        var isEnableAnimation = true

        /**
         * 动画时长
         */
        @IntRange(from = 0)
        var duration: Long = APP.resourcesInteger(android.R.integer.config_mediumAnimTime).toLong()

        /**
         * 进入动画
         */
        var enterAnimation: Animation = AlphaAnimation(0f, 1f).apply { duration = duration }

        /**
         * 退出动画
         */
        var exitAnimation: Animation = AlphaAnimation(1f, 0f).apply { duration = duration }
    }

}