package chooongg.box.http

import android.app.Application
import android.util.Log

object HttpBox {

    @JvmStatic
    fun initialize(application: Application) {
        Log.e("CHOOONGG", "initialize: HTTP")
    }

    @JvmStatic
    fun onTerminate() {

    }
}