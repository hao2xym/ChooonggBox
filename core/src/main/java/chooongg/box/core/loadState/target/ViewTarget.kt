package chooongg.box.core.loadState.target

import android.view.View
import android.view.ViewGroup
import chooongg.box.core.R
import chooongg.box.core.loadState.LoadLayout
import chooongg.box.core.loadState.callback.Callback
import chooongg.box.core.loadState.callback.SuccessCallback
import kotlin.reflect.KClass

class ViewTarget : ITarget {
    override fun withEquals(target: Any?) = target is View

    override fun replaceView(
        target: Any,
        onReloadListener: ((KClass<out Callback>) -> Unit)?
    ): LoadLayout {
        val oldContent = target as View
        val background = oldContent.background
        val contentParent = oldContent.parent as? ViewGroup
        var childIndex = 0
        val childCount = contentParent?.childCount ?: 0
        for (i in 0 until childCount) {
            if (contentParent!!.getChildAt(i) == oldContent) {
                childIndex = i
                break
            }
        }
        contentParent?.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(oldContent.context, onReloadListener)
        loadLayout.background = background
        loadLayout.setupSuccessLayout(
            SuccessCallback(
                oldContent,
                oldContent.context,
                onReloadListener
            )
        )
        loadLayout.id = R.id.load_layout
        contentParent?.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}