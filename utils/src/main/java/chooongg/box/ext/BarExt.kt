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
import android.util.TypedValue
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import androidx.annotation.ColorInt
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.fragment.app.Fragment
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
        showStatusBarView(window)
        addMarginTopEqualStatusBarHeight(window)
    } else {
        addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        hideStatusBarView(window)
        subtractMarginTopEqualStatusBarHeight(window)
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
 * 设置状态栏深色模式
 */
fun Activity.setStatusBarLightMode(isLightMode: Boolean) = window.setStatusBarLightMode(isLightMode)

/**
 * 设置状态栏深色模式
 */
fun Fragment.setStatusBarLightModel(isLightMode: Boolean) =
    activity?.setStatusBarLightMode(isLightMode)

/**
 * 设置状态栏深色模式
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
 * 状态栏是否是深色模式
 */
fun Fragment.isStatusBarLightMode() = requireActivity().isStatusBarLightMode()

/**
 * 状态栏是否是深色模式
 */
fun Activity.isStatusBarLightMode() = window.isStatusBarLightMode()

/**
 * 状态栏是否是深色模式
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
    transparentStatusBar(activity)
    return applyStatusBarColor(activity, color, isDecor)
}

/**
 * 设置状态栏颜色
 */
fun Window.setStatusBarColor(@ColorInt color: Int, isDecor: Boolean = false): View? {
    transparentStatusBar(window)
    return applyStatusBarColor(window, color, isDecor)
}

/**
 * Set the status bar's color.
 *
 * @param fakeStatusBar The fake status bar view.
 * @param color         The status bar's color.
 */
fun setStatusBarColor(fakeStatusBar: View, @ColorInt color: Int) {
    val activity: Activity = fakeStatusBar.context.getActivity() ?: return
    transparentStatusBar(activity)
    fakeStatusBar.visibility = View.VISIBLE
    val layoutParams = fakeStatusBar.layoutParams
    layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
    layoutParams.height = getStatusBarHeight()
    fakeStatusBar.setBackgroundColor(color)
}

/**
 * Set the custom status bar.
 *
 * @param fakeStatusBar The fake status bar view.
 */
fun setStatusBarCustom(fakeStatusBar: View) {
    val activity: Activity = fakeStatusBar.context.getActivity() ?: return
    transparentStatusBar(activity)
    fakeStatusBar.visibility = View.VISIBLE
    var layoutParams = fakeStatusBar.layoutParams
    if (layoutParams == null) {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getStatusBarHeight()
        )
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

fun transparentStatusBar(activity: Activity) {
    transparentStatusBar(activity.window)
}

fun transparentStatusBar(window: Window) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        val option = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        val vis = window.decorView.systemUiVisibility
        window.decorView.systemUiVisibility = option or vis
        window.statusBarColor = Color.TRANSPARENT
    } else {
        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    }
}

///////////////////////////////////////////////////////////////////////////
// action bar
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// action bar
///////////////////////////////////////////////////////////////////////////
/**
 * Return the action bar's height.
 *
 * @return the action bar's height
 */
fun getActionBarHeight(): Int {
    val tv = TypedValue()
    return if (Utils.getApp().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
        TypedValue.complexToDimensionPixelSize(
            tv.data, Resources.getSystem().displayMetrics
        )
    } else 0
}

///////////////////////////////////////////////////////////////////////////
// notification bar
///////////////////////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////////////////////
// notification bar
///////////////////////////////////////////////////////////////////////////
/**
 * Set the notification bar's visibility.
 *
 * Must hold `<uses-permission android:name="android.permission.EXPAND_STATUS_BAR" />`
 *
 * @param isVisible True to set notification bar visible, false otherwise.
 */
@RequiresPermission(EXPAND_STATUS_BAR)
fun setNotificationBarVisibility(isVisible: Boolean) {
    val methodName: String
    methodName = if (isVisible) {
        if (Build.VERSION.SDK_INT <= 16) "expand" else "expandNotificationsPanel"
    } else {
        if (Build.VERSION.SDK_INT <= 16) "collapse" else "collapsePanels"
    }
    invokePanels(methodName)
}

private fun invokePanels(methodName: String) {
    try {
        @SuppressLint("WrongConstant") val service: Any =
            Utils.getApp().getSystemService("statusbar")
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
 * Return the navigation bar's height.
 *
 * @return the navigation bar's height
 */
fun getNavBarHeight(): Int {
    val res = Resources.getSystem()
    val resourceId = res.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId != 0) {
        res.getDimensionPixelSize(resourceId)
    } else {
        0
    }
}

/**
 * Set the navigation bar's visibility.
 *
 * @param activity  The activity.
 * @param isVisible True to set navigation bar visible, false otherwise.
 */
fun setNavBarVisibility(activity: Activity, isVisible: Boolean) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
    setNavBarVisibility(activity.window, isVisible)
}

/**
 * Set the navigation bar's visibility.
 *
 * @param window    The window.
 * @param isVisible True to set navigation bar visible, false otherwise.
 */
fun setNavBarVisibility(window: Window, isVisible: Boolean) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return
    val decorView = window.decorView as ViewGroup
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
 * Return whether the navigation bar visible.
 *
 * Call it in onWindowFocusChanged will get right result.
 *
 * @param activity The activity.
 * @return `true`: yes<br></br>`false`: no
 */
fun isNavBarVisible(activity: Activity): Boolean {
    return isNavBarVisible(activity.window)
}

/**
 * Return whether the navigation bar visible.
 *
 * Call it in onWindowFocusChanged will get right result.
 *
 * @param window The window.
 * @return `true`: yes<br></br>`false`: no
 */
fun isNavBarVisible(window: Window): Boolean {
    var isVisible = false
    val decorView = window.decorView as ViewGroup
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
        if (UtilsBridge.isSamsung()
            && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && Build.VERSION.SDK_INT < Build.VERSION_CODES.Q
        ) {
            try {
                return Settings.Global.getInt(
                    Utils.getApp().getContentResolver(),
                    "navigationbar_hide_bar_enabled"
                ) === 0
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
        Utils.getApp().getResources().getResourceEntryName(id)
    } catch (ignore: Exception) {
        ""
    }
}

/**
 * Set the navigation bar's color.
 *
 * @param activity The activity.
 * @param color    The navigation bar's color.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun setNavBarColor(activity: Activity, @ColorInt color: Int) {
    setNavBarColor(activity.window, color)
}

/**
 * Set the navigation bar's color.
 *
 * @param window The window.
 * @param color  The navigation bar's color.
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun setNavBarColor(window: Window, @ColorInt color: Int) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.navigationBarColor = color
}

/**
 * Return the color of navigation bar.
 *
 * @param activity The activity.
 * @return the color of navigation bar
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun getNavBarColor(activity: Activity): Int {
    return getNavBarColor(activity.window)
}

/**
 * Return the color of navigation bar.
 *
 * @param window The window.
 * @return the color of navigation bar
 */
@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun getNavBarColor(window: Window): Int {
    return window.navigationBarColor
}

/**
 * Return whether the navigation bar visible.
 *
 * @return `true`: yes<br></br>`false`: no
 */
fun isSupportNavBar(): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
        val wm = Utils.getApp().getSystemService(Context.WINDOW_SERVICE) as WindowManager
            ?: return false
        val display = wm.defaultDisplay
        val size = Point()
        val realSize = Point()
        display.getSize(size)
        display.getRealSize(realSize)
        return realSize.y !== size.y || realSize.x !== size.x
    }
    val menu = ViewConfiguration.get(Utils.getApp()).hasPermanentMenuKey()
    val back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
    return !menu && !back
}

/**
 * Set the nav bar's light mode.
 *
 * @param activity    The activity.
 * @param isLightMode True to set nav bar light mode, false otherwise.
 */
fun setNavBarLightMode(
    activity: Activity,
    isLightMode: Boolean
) {
    setNavBarLightMode(activity.window, isLightMode)
}

/**
 * Set the nav bar's light mode.
 *
 * @param window      The window.
 * @param isLightMode True to set nav bar light mode, false otherwise.
 */
fun setNavBarLightMode(
    window: Window,
    isLightMode: Boolean
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val decorView = window.decorView
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
 * Is the nav bar light mode.
 *
 * @param activity The activity.
 * @return `true`: yes<br></br>`false`: no
 */
fun isNavBarLightMode(activity: Activity): Boolean {
    return isNavBarLightMode(activity.window)
}

/**
 * Is the nav bar light mode.
 *
 * @param window The window.
 * @return `true`: yes<br></br>`false`: no
 */
fun isNavBarLightMode(window: Window): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val decorView = window.decorView
        val vis = decorView.systemUiVisibility
        return vis and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR != 0
    }
    return false
}