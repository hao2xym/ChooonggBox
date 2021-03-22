package chooongg.box.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes
import chooongg.box.manager.AppManager

/**
 * Toast唯一实例
 */
private var boxToast: Toast? = null

/**
 * 展示Toast
 */
fun showToast(
    text: CharSequence?,
    duration: Int = Toast.LENGTH_SHORT,
    block: ((Toast) -> Unit)? = null
) {
    boxToast?.cancel()
    boxToast = Toast.makeText(AppManager.activityTop as? Context ?: APP, text, duration).apply {
        block?.invoke(this)
        show()
    }
}

/**
 * 展示Toast
 */
fun showToast(
    @StringRes resId: Int,
    duration: Int = Toast.LENGTH_SHORT,
    block: ((Toast) -> Unit)? = null
) {
    boxToast?.cancel()
    boxToast = Toast.makeText(AppManager.activityTop as? Context ?: APP, resId, duration).apply {
        block?.invoke(this)
        show()
    }
}

/**
 * 取消Toast
 */
fun cancelToast() {
    boxToast?.cancel()
}