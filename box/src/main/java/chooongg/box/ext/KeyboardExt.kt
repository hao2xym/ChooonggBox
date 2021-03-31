package chooongg.box.ext

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Rect
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * 显示软键盘
 */
fun showSoftInput(view: View?) {
    view?.post {
        val imm = view.context.inputMethodManager
        view.isFocusable = true
        view.isFocusableInTouchMode = true
        view.requestFocus()
        imm.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }
}

/**
 * 隐藏软键盘
 */
fun Context.hideSoftInput() {
    val activity = getActivity()
    val view = activity?.currentFocus
    if (view != null) {
        val imm = activity.inputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * 隐藏软键盘
 */
fun hideSoftInput(dialog: Dialog) {
    val view = dialog.window?.peekDecorView()
    if (view != null) {
        val inputMethodManager = dialog.context.inputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

/**
 * 切换键盘显示与否状态
 */
fun toggleSoftInput() {
    val imm = APP.inputMethodManager
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

/**
 * 判断软键盘是否可见
 *
 * @param activity             Activity
 * @param minHeightOfSoftInput 输入法最小高度
 * @return `true`: yes<br></br>`false`: no
 */
fun isSoftInputVisible(activity: Activity, minHeightOfSoftInput: Int = 200): Boolean {
    return getContentViewInvisibleHeight(activity) >= minHeightOfSoftInput
}

/**
 * 软键盘显示监听
 */
fun Activity.onKeyboardShowListener(listener: (isShow: Boolean) -> Unit) {
    window.contentView.viewTreeObserver.addOnGlobalLayoutListener {
        val rect = Rect()
        window.decorView.getWindowVisibleDisplayFrame(rect)
        val screenHeight = window.decorView.rootView.height
        val heightDifference = screenHeight - rect.bottom
        if (heightDifference > screenHeight / 3) {
            listener.invoke(true)
        } else {
            listener.invoke(false)
        }
    }
}

private fun getContentViewInvisibleHeight(activity: Activity): Int {
    val contentView = activity.findViewById<View>(android.R.id.content)
    val outRect = Rect()
    contentView.getWindowVisibleDisplayFrame(outRect)
    return contentView.bottom - outRect.bottom
}

/**
 * 修复软键盘内存泄漏
 *
 * [Activity.onDestroy]在销毁时调用该方法
 */
fun fixSoftInputLeaks(context: Context?) {
    if (context == null) return
    val imm = APP.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    val strArr = arrayOf("mCurRootView", "mServedView", "mNextServedView")
    for (i in 0..2) {
        try {
            val declaredField = imm.javaClass.getDeclaredField(strArr[i])
            if (!declaredField.isAccessible) declaredField.isAccessible = true
            val obj = declaredField.get(imm)
            if (obj == null || obj !is View) continue
            if (obj.context == context) declaredField.set(imm, null) else return
        } catch (th: Throwable) {
            th.printStackTrace()
        }
    }
}