package chooongg.box.core.loadState.target

import android.app.Activity
import chooongg.box.core.R
import chooongg.box.core.loadState.LoadLayout
import chooongg.box.core.loadState.callback.Callback
import chooongg.box.core.loadState.callback.SuccessCallback
import chooongg.box.core.widget.BoxToolBar
import chooongg.box.ext.contentView
import kotlin.reflect.KClass

class ActivityTarget : ITarget {

    override fun withEquals(target: Any?) = target is Activity

    override fun replaceView(
        target: Any,
        onReloadListener: ((KClass<out Callback>) -> Unit)?
    ): LoadLayout {
        val activity = target as Activity
        val contentParent = activity.contentView
        var childIndex = 0
        for (i in 0 until contentParent.childCount) {
            if (contentParent.getChildAt(i) !is BoxToolBar) {
                childIndex = i
                break
            }
        }
        val oldContent = contentParent.getChildAt(childIndex)
        val background = oldContent.background
        contentParent.removeView(oldContent)
        val oldLayoutParams = oldContent.layoutParams
        val loadLayout = LoadLayout(activity, onReloadListener)
        loadLayout.background = background
        loadLayout.setupSuccessLayout(SuccessCallback(oldContent, activity, null))
        loadLayout.id = R.id.load_layout
        contentParent.addView(loadLayout, childIndex, oldLayoutParams)
        return loadLayout
    }
}