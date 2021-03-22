package chooongg.box.ext

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat


fun Context.getScreenWidth() = resources.displayMetrics.widthPixels

fun Context.getScreenHeight() = resources.displayMetrics.heightPixels

fun getScreenDensity() = Resources.getSystem().displayMetrics.density

fun getScreenDensityDpi() = Resources.getSystem().displayMetrics.densityDpi

fun getScreenXDpi() = Resources.getSystem().displayMetrics.xdpi

fun getScreenYDpi() = Resources.getSystem().displayMetrics.ydpi

fun getScreenScaledDensity() = Resources.getSystem().displayMetrics.scaledDensity

fun Context.isLandscape() =
    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun Context.isPortrait() =
    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(): Int {
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

/**
 * 显示系统栏
 */
fun Activity.showSystemBars() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.show(WindowInsetsCompat.Type.systemBars())
}

/**
 * 隐藏系统栏
 */
fun Activity.hideSystemBars() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.hide(WindowInsetsCompat.Type.systemBars())
}

/**
 * 显示状态栏
 */
fun Activity.showStatusBars() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.show(WindowInsetsCompat.Type.statusBars())
}

/**
 * 隐藏状态栏
 */
fun Activity.hideStatusBars() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.hide(WindowInsetsCompat.Type.statusBars())
}

/**
 * 显示导航栏
 */
fun Activity.showNavigationBars() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.show(WindowInsetsCompat.Type.navigationBars())
}

/**
 * 隐藏导航栏
 */
fun Activity.hideNavigationBars() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.hide(WindowInsetsCompat.Type.navigationBars())
}

/**
 * 显示输入法
 */
fun Activity.showInputMethodEditor() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.show(WindowInsetsCompat.Type.ime())
}

/**
 * 隐藏输入法
 */
fun Activity.hideInputMethodEditor() {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.hide(WindowInsetsCompat.Type.ime())
}

/**
 * 设置亮色状态栏
 */
fun Activity.setLightStatusBars(isLightMode: Boolean) {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.isAppearanceLightStatusBars = isLightMode
}

/**
 * 是否是亮色状态栏
 */
fun Activity.isLightStatusBars(): Boolean? {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return null
    return insetsController.isAppearanceLightStatusBars
}

/**
 * 设置亮色导航栏
 */
fun Activity.setLightNavigationBars(isLightMode: Boolean) {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return
    insetsController.isAppearanceLightNavigationBars = isLightMode
}

/**
 * 是否是亮色导航栏
 */
fun Activity.isLightNavigationBars(): Boolean? {
    val insetsController = WindowCompat.getInsetsController(window, decorView) ?: return null
    return insetsController.isAppearanceLightNavigationBars
}