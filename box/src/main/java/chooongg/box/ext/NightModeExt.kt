package chooongg.box.ext

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import chooongg.box.entity.BoxMMKVConst

/**
 * 判断当前是否深色模式
 */
fun Context.isNightMode(): Boolean {
    return when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
        Configuration.UI_MODE_NIGHT_YES -> true
        else -> false
    }
}

fun Application.initNightMode() {
    AppCompatDelegate.setDefaultNightMode(BoxMMKVConst.DayNightMode.decode())
}

/**
 * 设置深色模式
 */
fun setNightMode(@AppCompatDelegate.NightMode mode: Int) {
    BoxMMKVConst.DayNightMode.encode(mode)
    AppCompatDelegate.setDefaultNightMode(mode)
}