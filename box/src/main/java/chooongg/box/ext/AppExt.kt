package chooongg.box.ext

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

/**
 * 全局 Application 常量
 */
inline val APP get() = chooongg.box.manager.AppManager.getApplication()

/**
 * 判断是否是 Debug 版本
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
 * Debug 才运行的代码块
 */
fun debug(init: () -> Unit) {
    if (isAppDebug()) init()
}

/**
 * Release 才运行的代码块
 */
fun release(init: () -> Unit) {
    if (!isAppDebug()) init()
}