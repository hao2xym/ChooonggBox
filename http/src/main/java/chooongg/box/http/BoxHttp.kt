package chooongg.box.http

import android.app.Application
import android.util.Log
import chooongg.box.Box

object BoxHttp {
    @JvmStatic
    fun initialize(application: Application) {
        Log.d(Box.TAG, "initialize: ${BuildConfig.LIBRARY_PACKAGE_NAME}")
    }
}