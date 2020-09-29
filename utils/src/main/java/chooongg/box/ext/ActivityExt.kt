package chooongg.box.ext

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.view.View
import android.view.Window
import android.widget.FrameLayout
import androidx.appcompat.widget.ContentFrameLayout
import androidx.fragment.app.Fragment

const val CLICK_INTERVAL = 800L

inline val Activity.decorView: FrameLayout get() = window.decorView as FrameLayout
inline val Activity.contentView: ContentFrameLayout get() = findViewById(Window.ID_ANDROID_CONTENT)
inline val Window.contentView: ContentFrameLayout get() = findViewById(Window.ID_ANDROID_CONTENT)

fun Context.loadActivityLabel() = getActivity()?.loadActivityLabel()
fun Fragment.loadActivityLabel() = activity?.loadActivityLabel()
fun Activity.loadActivityLabel(): CharSequence? {
    val activityInfo = packageManager.getActivityInfo(ComponentName(this, javaClass), 0)
    return activityInfo.loadLabel(packageManager)
}