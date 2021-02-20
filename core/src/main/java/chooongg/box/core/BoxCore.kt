package chooongg.box.core

import android.app.Application
import android.util.Log
import chooongg.box.Box

object BoxCore {

    @JvmStatic
    fun init(application: Application) {
        Log.d(Box.TAG, "init: ${BuildConfig.LIBRARY_PACKAGE_NAME}")
    }
}