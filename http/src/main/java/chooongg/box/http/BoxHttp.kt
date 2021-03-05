package chooongg.box.http

import android.app.Application
import android.util.Log
import androidx.annotation.Keep
import chooongg.box.Box
import chooongg.box.entity.MemoryConstants

@Keep
object BoxHttp {

    const val DEFAULT_TIMEOUT = 30L // 默认超时时间 秒为单位
    const val DEFAULT_HTTP_CACHE_SIZE = 10L * MemoryConstants.MB // 默认缓存大小

    @Keep
    @JvmStatic
    @Suppress("unused", "UNUSED_PARAMETER")
    fun initialize(application: Application) {
        Log.d(Box.TAG, "[initialize: ${BuildConfig.LIBRARY_PACKAGE_NAME}]")
    }
}