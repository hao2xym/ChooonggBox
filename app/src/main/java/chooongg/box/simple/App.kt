package chooongg.box.simple

import android.app.Application

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        "sdf".isBlank()
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}