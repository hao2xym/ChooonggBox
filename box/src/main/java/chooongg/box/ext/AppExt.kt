package chooongg.box.ext

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import com.tencent.mmkv.BuildConfig

/**
 * 全局Application常量
 */
inline val APP get() = chooongg.box.manager.AppManager.getApplication()

/**
 * 判断是否是Debug版本
 *
 * @param packageName 包名-默认本App包名
 */
fun isAppDebug(packageName: String = APP.packageName): Boolean {
    if (packageName.isBlank()) return false
    return try {
        val ai = APP.packageManager.getApplicationInfo(packageName, 0)
        ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        false
    }
}

/**
 * Debug才运行的代码块
 */
fun debug(init: () -> Unit) {
    if (BuildConfig.DEBUG) init()
}

/**
 * Release才运行的代码块
 */
fun unDebug(init: () -> Unit) {
    if (!BuildConfig.DEBUG) init()
}