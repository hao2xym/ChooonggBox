package chooongg.box.ext

import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

/* Debug才运行的代码块 */
fun debug(init: () -> Unit) {
    if (isDebug()) init()
}

/* Release才运行的代码块*/
fun release(init: () -> Unit) {
    if (!isDebug()) init()
}

/**
 * 判断是否是Debug版本
 *
 * @param packageName 包名-默认本App包名
 */
fun isDebug(packageName: String = APP.packageName): Boolean {
    if (packageName.isBlank()) return false
    return try {
        val ai = APP.packageManager.getApplicationInfo(packageName, 0)
        ai.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        false
    }
}