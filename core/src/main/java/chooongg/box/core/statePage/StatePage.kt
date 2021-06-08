package chooongg.box.core.statePage

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import chooongg.box.core.statePage.state.MultiState

object StatePage {

    internal val config = StatePageConfig()

    public fun setDefaultConfig(block: (StatePageConfig) -> Unit) {
        block.invoke(config)
    }

    /**
     * View绑定状态布局
     */
    fun bindStatePage(
        targetView: View,
        onRetryEventListener: ((MultiState) -> Unit)? = null
    ): StatePageLayout {
        val parent = targetView.parent as ViewGroup?
        var targetViewIndex = 0
        val statePageLayout = StatePageLayout(targetView.context, targetView, onRetryEventListener)
        parent?.let { targetViewParent ->
            for (i in 0 until targetViewParent.childCount) {
                if (targetViewParent.getChildAt(i) == targetView) {
                    targetViewIndex = i
                    break
                }
            }
            targetViewParent.removeView(targetView)
            targetViewParent.addView(statePageLayout, targetViewIndex, targetView.layoutParams)
        }
        statePageLayout.initialization()
        return statePageLayout
    }

    /**
     * Activity绑定状态布局
     */
    fun bindStatePage(
        activity: Activity,
        onRetryEventListener: ((MultiState) -> Unit)? = null
    ): StatePageLayout {
        val targetView = activity.findViewById<ViewGroup>(android.R.id.content)
        val targetViewIndex = 0
        val oldContent: View = targetView.getChildAt(targetViewIndex)
        targetView.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val statePageLayout = StatePageLayout(oldContent.context, oldContent, onRetryEventListener)
        targetView.addView(statePageLayout, targetViewIndex, oldLayoutParams)
        statePageLayout.initialization()
        return statePageLayout
    }
}