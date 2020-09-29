package chooongg.box.ext

import android.Manifest.permission.EXPAND_STATUS_BAR
import android.R
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.Point
import android.os.Build
import android.provider.Settings
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import android.view.WindowManager
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
import chooongg.box.utils.RomUtils
import java.lang.reflect.Method


private const val TAG_STATUS_BAR = "TAG_STATUS_BAR"
private const val TAG_OFFSET = "TAG_OFFSET"
private const val KEY_OFFSET = -123

/**
 * 获取状态栏高度
 */
fun getStatusBarHeight(): Int {
    val resources = Resources.getSystem()
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    return resources.getDimensionPixelSize(resourceId)
}

/**
 * 设置状态栏可见
 */
fun Activity.setStatusBarVisibility(isVisible: Boolean) = window.setStatusBarVisibility(isVisible)

/**
 * 设置状态栏可见
 */
fun Fragment.setStatusBarVisibility(isVisible: Boolean) =
    activity?.setStatusBarVisibility(isVisible)

/**
 * 设置状态栏可见
 */
fun Window.setStatusBarVisibility(isVisible: Boolean) {
    if (isVisible) {
        clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        showStatusBarView(this)
        addMarginTopEqualStatusBarHeight()
    } else {
        addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hideStatusBarView(this)
        subtractMarginTopEqualStatusBarHeight()
    }
}

/**
 * 状态栏是否可见
 */
fun Fragment.isStatusBarVisible() = requireActivity().isStatusBarVisible()

/**
 * 设置状态栏深色模式
 */
fun Activity.isStatusBarVisible() =
    window.attributes.flags and WindowManager.LayoutParams.FLAG_FULLSCREEN == 0

/**
 * 设置状态栏亮色模式
 */
fun Activity.setStatusBarLightMode(isLightMode: Boolean) = window.setStatusBarLightMode(isLightMode)

/**
 * 设置状态栏亮色模式
 */
fun Fragment.setStatusBarLightModel(isLightMode: Boolean) =
    activity?.setStatusBarLightMode(isLightMode)

/**
 * 设置状态栏亮色模式
 */
fun Window.setStatusBarLightMode(isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        var vis = decorView.systemUiVisibility
        vis = if (isLightMode) vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        else vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        decorView.systemUiVisibility = vis
    }
}

/**
 * 状态栏是否是亮色模式
 */
fun Fragment.isStatusBarLightMode() = requireActivity().isStatusBarLightMode()

/**
 * 状态栏是否是亮色模式
 */
fun Activity.isStatusBarLightMode() = window.isStatusBarLightMode()

/**
 * 状态栏是否是亮色模式
 */
fun Window.isStatusBarLightMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val vis = decorView.systemUiVisibility
        return vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR != 0
    }
    return false
}

/**
 * 为View增加状态栏高度的顶部外边距
 */
private fun Window.addMarginTopEqualStatusBarHeight() {
    val withTag = decorView.findViewWithTag<View>(TAG_OFFSET) ?: return
    withTag.addMarginTopEqualStatusBarHeight()
}

/**
 * 为View增加状态栏高度的顶部外边距
 */
fun View.addMarginTopEqualStatusBarHeight() {
    tag = TAG_OFFSET
    val haveSetOffset = getTag(KEY_OFFSET)
    if (haveSetOffset != null && haveSetOffset as Boolean) return
    val layoutParams = layoutParams as MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        layoutParams.topMargin + getStatusBarHeight(),
        layoutParams.rightMargin,
        layoutParams.bottomMargin
    )
    setTag(KEY_OFFSET, true)
}

/**
 * 为View减去状态栏高度的顶部外边距
 */
private fun Window.subtractMarginTopEqualStatusBarHeight() {
    val withTag = decorView.findViewWithTag<View>(TAG_OFFSET) ?: return
    withTag.subtractMarginTopEqualStatusBarHeight()
}

/**
 * 为View减去状态栏高度的顶部外边距
 */
fun View.subtractMarginTopEqualStatusBarHeight() {
    val haveSetOffset = getTag(KEY_OFFSET)
    if (haveSetOffset == null || !(haveSetOffset as Boolean)) return
    val layoutParams = layoutParams as MarginLayoutParams
    layoutParams.setMargins(
        layoutParams.leftMargin,
        layoutParams.topMargin - getStatusBarHeight(),
        layoutParams.rightMargin,
        layoutParams.bottomMargin
    )
    setTag(KEY_OFFSET, false)
}

/**
 * 设置状态栏颜色
 */
fun Activity.setStatusBarColor(@ColorInt color: Int, isDecor: Boolean): View? {
    transparentStatusBar()
    return applyStatusBarColor(this, color, isDecor)
}

/**
 * 设置状态栏颜色
 */
fun Fragment.setStatusBarColor(@ColorInt color: Int, isDecor: Boolean) =
    activity?.setStatusBarColor(color, isDecor)

/**
 * 设置状态栏颜色
 */
fun Window.setStatusBarColor(@ColorInt color: Int, isDecor: Boolean = false): View? {
    transparentStatusBar()
    return applyStatusBarColor(this, color, isDecor)
}

/**
 * 设置状态栏颜色
 */
fun setStatusBarColor(fakeStatusBar: View, @ColorInt color: Int) {
    val activity: Activity = fakeStatusBar.context.getActivity() ?: return
    activity.transparentStatusBar()
    fakeStatusBar.visibility = View.VISIBLE
    val layoutParams = fakeStatusBar.layoutParams
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    layoutParams.height = getStatusBarHeight()
    fakeStatusBar.setBackgroundColor(color)
}

/**
 * 设置自定义状态栏
 */
fun setStatusBarCustom(fakeStatusBar: View) {
    val activity: Activity = fakeStatusBar.context.getActivity() ?: return
    activity.transparentStatusBar()
    fakeStatusBar.visibility = View.VISIBLE
    var layoutParams = fakeStatusBar.layoutParams
    if (layoutParams == null) {
        layoutParams =
            ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight())
        fakeStatusBar.layoutParams = layoutParams
    } else {
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        layoutParams.height = getStatusBarHeight()
    }
}

private fun applyStatusBarColor(activity: Activity, color: Int, isDecor: Boolean): View? {
    return applyStatusBarColor(activity.window, color, isDecor)
}

private fun applyStatusBarColor(window: Window, color: Int, isDecor: Boolean): View? {
    val parent =
        if (isDecor) window.decorView as ViewGroup else window.contentView
    var fakeStatusBarView = parent.findViewWithTag<View>(TAG_STATUS_BAR)
    if (fakeStatusBarView != null) {
        if (fakeStatusBarView.visibility == View.GONE) {
            fakeStatusBarView.visible()
        }
        fakeStatusBarView.setBackgroundColor(color)
    } else {
        fakeStatusBarView = createStatusBarView(window.context, color)
        parent.addView(fakeStatusBarView)
    }
    return fakeStatusBarView
}

private fun hideStatusBarView(activity: Activity) {
    hideStatusBarView(activity.window)
}

private fun hideStatusBarView(window: Window) {
    val decorView = window.decorView as ViewGroup
    val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
    fakeStatusBarView.visibility = View.GONE
}

private fun showStatusBarView(window: Window) {
    val decorView = window.decorView as ViewGroup
    val fakeStatusBarView = decorView.findViewWithTag<View>(TAG_STATUS_BAR) ?: return
    fakeStatusBarView.visibility = View.VISIBLE
}

private fun createStatusBarView(
    context: Context,
    color: Int
): View? {
    val statusBarView = View(context)
    statusBarView.layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight()
    )
    statusBarView.setBackgroundColor(color)
    statusBarView.tag = TAG_STATUS_BAR
    return statusBarView
}

fun Activity.transparentStatusBar() {
    window.transparentStatusBar()
}

fun Window.transparentStatusBar() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = decorView.systemUiVisibility
        decorView.systemUiVisibility = option or vis
        statusBarColor = Color.TRANSPARENT
    } else addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
}

///////////////////////////////////////////////////////////////////////////
// action bar
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// action bar
///////////////////////////////////////////////////////////////////////////
/**
 * 获取标题栏高度
 */
fun getActionBarHeight(): Int {
    val tv = TypedValue()
    return if (APP.theme.resolveAttribute(R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(tv.data, Resources.getSystem().displayMetrics)
    } else 0
}

///////////////////////////////////////////////////////////////////////////
// notification bar
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// notification bar
///////////////////////////////////////////////////////////////////////////
/**
 * 设置通知栏可见
 */
@RequiresPermission(EXPAND_STATUS_BAR)
fun setNotificationBarVisibility(isVisible: Boolean) {
    val methodName = if (isVisible) "expandNotificationsPanel" else "collapsePanels"
    invokePanels(methodName)
}

private fun invokePanels(methodName: String) {
    try {
        @SuppressLint("WrongConstant") val service: Any =
            APP.getSystemService("statusbar")
        @SuppressLint("PrivateApi") val statusBarManager =
            Class.forName("android.app.StatusBarManager")
        val expand: Method = statusBarManager.getMethod(methodName)
        expand.invoke(service)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

///////////////////////////////////////////////////////////////////////////
// navigation bar
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// navigation bar
///////////////////////////////////////////////////////////////////////////
/**
 * 获取导航栏高度
 */
fun getNavigationBarHeight(): Int {
    val res = Resources.getSystem()
    val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId != 0) {
        res.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

/**
 * 设置导航栏可见
 */
fun Activity.setNavigationBarVisibility(isVisible: Boolean) =
    window.setNavigationBarVisibility(isVisible)

/**
 * 设置导航栏可见
 */
fun Window.setNavigationBarVisibility(isVisible: Boolean) {
    val decorView = decorView as ViewGroup
    var i = 0
    val count = decorView.childCount
    while (i < count) {
        val child = decorView.getChildAt(i)
        val id = child.id
        if (id != View.NO_ID) {
            val resourceEntryName = getResNameById(id)
            if ("navigationBarBackground" == resourceEntryName) {
                child.visibility = if (isVisible) View.VISIBLE else View.INVISIBLE
            }
        }
        i++
    }
    val uiOptions = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
    if (isVisible) {
        decorView.systemUiVisibility = decorView.systemUiVisibility and uiOptions.inv()
    } else {
        decorView.systemUiVisibility = decorView.systemUiVisibility or uiOptions
    }
}

/**
 * 导航栏是否可见
 */
fun Activity.isNavgationBarVisible() = window.isNavgationBarVisible()

/**
 * 导航栏是否可见
 */
fun Window.isNavgationBarVisible(): Boolean {
    var isVisible = false
    val decorView = decorView as ViewGroup
    var i = 0
    val count = decorView.childCount
    while (i < count) {
        val child = decorView.getChildAt(i)
        val id = child.id
        if (id != View.NO_ID) {
            val resourceEntryName = getResNameById(id)
            if ("navigationBarBackground" == resourceEntryName && child.visibility == View.VISIBLE) {
                isVisible = true
                break
            }
        }
        i++
    }
    if (isVisible) {
        // 对于三星手机，android10以下非OneUI2的版本，比如 s8，note8 等设备上，
        // 导航栏显示存在bug："当用户隐藏导航栏时显示输入法的时候导航栏会跟随显示"，会导致隐藏输入法之后判断错误
        // 这个问题在 OneUI 2 & android 10 版本已修复
        if (RomUtils.isSamsung() && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            try {
                return Settings.Global.getInt(
                    APP.contentResolver,
                    "navigationbar_hide_bar_enabled"
                ) == 0
            } catch (ignore: Exception) {
            }
        }
        val visibility = decorView.systemUiVisibility
        isVisible = visibility and View.SYSTEM_UI_FLAG_HIDE_NAVIGATION == 0
    }
    return isVisible
}

private fun getResNameById(id: Int): String {
    return try {
        APP.resources.getResourceEntryName(id)
    } catch (ignore: Exception) {
        ""
    }
}

/**
 * 设置导航栏颜色
 */
fun Activity.setNavigationBarColor(@ColorInt color: Int) = window.setNavigationBarColor(color)

/**
 * 设置导航栏颜色
 */
fun Window.setNavigationBarColor(@ColorInt color: Int) {
    addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    navigationBarColor = color
}

/**
 * 获取导航栏颜色
 */
fun Activity.getNavgationBarColor() = window.navigationBarColor

/**
 * 是否支持导航栏
 */
fun isSupportNavigationBar(): Boolean {
    val display = APP.windowManager.defaultDisplay
    val size = Point()
    val realSize = Point()
    display.getSize(size)
    display.getRealSize(realSize)
    return realSize.y != size.y || realSize.x != size.x
}

/**
 * 设置导航栏深色模式
 */
fun Activity.setNavigationBarLightMode(isLightMode: Boolean) =
    window.setNavigationBarLightMode(isLightMode)

/**
 * 设置导航栏深色模式
 */
fun Window.setNavigationBarLightMode(isLightMode: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        var vis = decorView.systemUiVisibility
        vis = if (isLightMode) {
            vis or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        } else {
            vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }
        decorView.systemUiVisibility = vis
    }
}

/**
 * 导航栏是否为深色模式
 */
fun Activity.isNavigationBarLightMode() = window.isNavigationBarLightMode()

/**
 * 导航栏是否为深色模式
 */
fun Window.isNavigationBarLightMode(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val vis = decorView.systemUiVisibility
        return vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR != 0
    }
    return false
}