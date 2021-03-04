package chooongg.box.core

import android.app.Application
import android.util.Log
import androidx.annotation.Keep
import chooongg.box.Box

@Keep
object BoxCore {

    @Keep
    @JvmStatic
    @Suppress("unused", "UNUSED_PARAMETER")
    fun initialize(application: Application) {
        Log.d(Box.TAG, "[initialize: ${BuildConfig.LIBRARY_PACKAGE_NAME}]")
    }
}