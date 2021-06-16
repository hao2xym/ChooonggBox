package chooongg.box.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.widget.ContentFrameLayout
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass


inline val Activity.decorView: FrameLayout get() = window.decorView as FrameLayout
inline val Activity.contentView: ContentFrameLayout get() = findViewById(Window.ID_ANDROID_CONTENT)
inline val Window.contentView: ContentFrameLayout get() = findViewById(Window.ID_ANDROID_CONTENT)

fun Fragment.loadActivityLabel() = activity?.loadActivityLabel()
fun Activity.loadActivityLabel(): CharSequence {
    val activityInfo = packageManager.getActivityInfo(ComponentName(this, javaClass), 0)
    return activityInfo.loadLabel(packageManager)
}

@SuppressLint("QueryPermissionsNeeded")
fun Context.getActivityForMain(`package`: CharSequence = packageName): Class<*>? {
    if (`package`.isEmpty()) return null
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.setPackage(`package`.toString())
    val list = packageManager.queryIntentActivities(intent, 0)
    if (list.isNullOrEmpty()) return null
    return Class.forName(list[0].activityInfo.packageName)
}

@SuppressLint("QueryPermissionsNeeded")
fun Activity.isMainActivity(): Boolean {
    val intent = Intent(Intent.ACTION_MAIN, null)
    intent.setPackage(packageName)
    val list = packageManager.queryIntentActivities(intent, 0)
    list.forEach {
        if (it.activityInfo.name.equals(this.javaClass.simpleName)) {
            return true
        }
    }
    return false
}

fun Context.startActivity(clazz: KClass<out Activity>, block: (ActivityIntent.() -> Unit)? = null) {
    val intent = ActivityIntent(this, clazz.java)
    block?.invoke(intent)
    startActivity(intent)
}

fun Context.startActivity(
    clazz: KClass<out Activity>,
    option: Bundle,
    block: (ActivityIntent.() -> Unit)? = null
) {
    val intent = ActivityIntent(this, clazz.java)
    block?.invoke(intent)
    startActivity(intent, option)
}

class ActivityIntent(packageContext: Context, cls: Class<*>) : Intent(packageContext, cls) {

    /**
     * 新活动成为新任务的根，旧的活动都被结束
     */
    fun launchClearTask() {
        addFlags(FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(FLAG_ACTIVITY_NEW_TASK)
    }

    fun launchSingleTop() {
        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
    }

    fun launchNewTask() {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun launchNoHistory() {
        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    }

    fun removeFlags() {
        flags = 0
    }
}