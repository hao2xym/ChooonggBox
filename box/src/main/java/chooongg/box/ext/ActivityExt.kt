package chooongg.box.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.ContentFrameLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.Fragment
import kotlin.reflect.KClass


inline val Activity.decorView: FrameLayout get() = window.decorView as FrameLayout
inline val Activity.contentView: ContentFrameLayout get() = findViewById(Window.ID_ANDROID_CONTENT)
inline val Window.contentView: ContentFrameLayout get() = findViewById(Window.ID_ANDROID_CONTENT)

fun Fragment.loadActivityLabel() = activity?.loadActivityLabel()
fun Context.loadActivityLabel(): CharSequence {
    val activity = getActivity() ?: return ""
    val activityInfo =
        packageManager.getActivityInfo(ComponentName(activity, activity.javaClass), 0)
    return activityInfo.loadLabel(packageManager)
}

/**
 * Activity是否活动
 */
fun Activity?.isLive() = this != null && !isFinishing && !isDestroyed

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

fun Context.startActivity(clazz: KClass<out Any>, block: (ActivityIntent.() -> Unit)? = null) {
    startActivity(clazz, getActivityOption(getActivity())?.toBundle(), block)
}

fun Context.startActivity(
    clazz: KClass<out Any>,
    view: View,
    block: (ActivityIntent.() -> Unit)? = null
) {
    startActivity(
        clazz, getActivityOption(getActivity(), Pair.create(view, "view"))?.toBundle(), block
    )
}

fun Context.startActivity(
    clazz: KClass<out Any>,
    vararg sharedElements: Pair<View, String>,
    block: (ActivityIntent.() -> Unit)? = null
) {
    startActivity(clazz, getActivityOption(getActivity(), *sharedElements)?.toBundle(), block)
}

fun Context.startActivity(
    clazz: KClass<out Any>,
    option: Bundle?,
    block: (ActivityIntent.() -> Unit)? = null
) {
    val intent = ActivityIntent(this, clazz.java)
    block?.invoke(intent)
    startActivity(intent, option)
}

fun Fragment.startActivity(
    clazz: KClass<out Any>,
    block: (ActivityIntent.() -> Unit)? = null
) {
    startActivity(clazz, getActivityOption(activity)?.toBundle(), block)
}

fun Fragment.startActivity(
    clazz: KClass<out Any>,
    view: View,
    block: (ActivityIntent.() -> Unit)? = null
) {
    startActivity(clazz, getActivityOption(activity, Pair.create(view, "view"))?.toBundle(), block)
}

fun Fragment.startActivity(
    clazz: KClass<out Any>,
    vararg sharedElements: Pair<View, String>,
    block: (ActivityIntent.() -> Unit)? = null
) {
    startActivity(clazz, getActivityOption(activity, *sharedElements)?.toBundle(), block)
}

fun Fragment.startActivity(
    clazz: KClass<out Any>,
    option: Bundle?,
    block: (ActivityIntent.() -> Unit)? = null
) {
    val intent = ActivityIntent(requireContext(), clazz.java)
    block?.invoke(intent)
    startActivity(intent, option)
}

fun ActivityResultLauncher<Intent>.launch(
    context: Context,
    clazz: KClass<out Any>,
    block: (ActivityIntent.() -> Unit)? = null
) {
    launch(context, clazz, getActivityOption(context.getActivity()))
}

fun ActivityResultLauncher<Intent>.launch(
    context: Context,
    clazz: KClass<out Any>,
    view: View,
    block: (ActivityIntent.() -> Unit)? = null
) {
    launch(context, clazz, getActivityOption(context.getActivity(), Pair.create(view, "view")))
}

fun ActivityResultLauncher<Intent>.launch(
    context: Context,
    clazz: KClass<out Any>,
    vararg sharedElements: Pair<View, String>,
    block: (ActivityIntent.() -> Unit)? = null
) {
    launch(context, clazz, getActivityOption(context.getActivity(), *sharedElements))
}

fun ActivityResultLauncher<Intent>.launch(
    context: Context,
    clazz: KClass<out Any>,
    option: ActivityOptionsCompat?,
    block: (ActivityIntent.() -> Unit)? = null
) {
    val intent = ActivityIntent(context, clazz.java)
    block?.invoke(intent)
    launch(intent, option)
}

fun getActivityOption(
    activity: Activity?,
    vararg sharedElements: Pair<View, String>
): ActivityOptionsCompat? {
    return if (activity != null) {
        if (sharedElements.size == 1) {
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                activity,
                Pair.create(sharedElements[0].first, sharedElements[0].second)
            )
        } else ActivityOptionsCompat.makeSceneTransitionAnimation(activity, *sharedElements)
    } else null
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