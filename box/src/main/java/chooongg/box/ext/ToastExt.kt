package chooongg.box.ext

import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Toast唯一实例
 */
private var boxToast: Toast? = null

/**
 * 展示Toast
 */
fun showToast(text: CharSequence?, duration: Int = Toast.LENGTH_SHORT) {
    if (boxToast == null) {
        boxToast = Toast.makeText(APP, text, duration)
        boxToast!!.show()
    } else {
        boxToast!!.cancel()
        boxToast = Toast.makeText(APP, text, duration)
        boxToast!!.show()
    }
}

/**
 * 展示Toast
 */
fun showToast(@StringRes resId: Int, duration: Int = Toast.LENGTH_SHORT) {
    if (boxToast == null) {
        boxToast = Toast.makeText(APP, resId, duration)
        boxToast!!.show()
    } else {
        boxToast!!.cancel()
        boxToast = Toast.makeText(APP, resId, duration)
        boxToast!!.show()
    }
}

/**
 * 取消Toast
 */
fun cancelToast() {
    boxToast?.cancel()
}