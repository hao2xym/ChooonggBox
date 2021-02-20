package chooongg.box.ext

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

fun Context.loadActivityLabel() = getActivity()?.loadActivityLabel()
fun Fragment.loadActivityLabel() = activity?.loadActivityLabel()
fun Activity.loadActivityLabel(): CharSequence {
    val activityInfo = packageManager.getActivityInfo(ComponentName(this, javaClass), 0)
    return activityInfo.loadLabel(packageManager)
}

fun Context.startActivity(
    clazz: KClass<out Activity>,
    block: (StartActivityConfig.() -> Unit)? = null
) {
    val intent = Intent(this, clazz.java)
    val startActivityConfig = StartActivityConfig(this, intent)
    block?.invoke(startActivityConfig)
    startActivityConfig.start()
}

class StartActivityConfig(private val context: Context, private val intent: Intent) {

    private var bundle: Bundle? = null

    /**
     * 新活动成为新任务的根，旧的活动都被结束
     */
    fun launchClearTask() {
        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    fun launch(){

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

    fun addFlags(flags: Int) {
        intent.addFlags(flags)
    }

    internal fun removeFlags(){
        intent.flags = 0
    }

    internal fun start() {
        context.startActivity(intent, bundle)
    }
}