package chooongg.box.http

import android.app.Application
import android.util.Log
import chooongg.box.Box

object HttpBox {

    @JvmStatic
    fun init(application: Application) {
        Log.d(Box.TAG, "init: ${BuildConfig.LIBRARY_PACKAGE_NAME}")
    }
}