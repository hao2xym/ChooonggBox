package chooongg.box.utils

import android.content.pm.ApplicationInfo
import chooongg.box.ext.APP

object AppUtils {

    /**
     * 是否是Debug应用
     */
    @JvmOverloads
    fun isAppDebug(packageName: String? = APP.packageName): Boolean {
        if (packageName.isNullOrEmpty()) return false
        val info = APP.applicationInfo
        return info != null && (info.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0
    }
}