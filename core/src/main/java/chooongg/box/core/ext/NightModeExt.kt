package chooongg.box.core.ext

import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate

/**
 * 判断当前是否深色模式
 */
fun Context.isNightMode(): Boolean {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }
}

/**
 * 设置深色模式
 */
fun setNightMode(@AppCompatDelegate.NightMode mode: Int) {
    AppCompatDelegate.setDefaultNightMode(mode)
}