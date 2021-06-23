package chooongg.box.manager

import chooongg.box.ext.APP
import com.facebook.stetho.Stetho

object StethoManager {

    private var enable = false

    fun isEnable() = enable

    fun initialization(isEnable: Boolean) {
        enable = isEnable
        if (isEnable) {
            Stetho.initializeWithDefaults(APP)
        }
    }
}