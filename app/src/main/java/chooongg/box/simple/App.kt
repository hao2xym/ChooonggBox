package chooongg.box.simple

import android.app.Activity
import android.app.Application
import android.os.Bundle
import chooongg.box.Box

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Box.initialize(this)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
            }

            override fun onActivityResumed(activity: Activity) {
            }

            override fun onActivityPaused(activity: Activity) {
            }

            override fun onActivityStopped(activity: Activity) {
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
            }

            override fun onActivityDestroyed(activity: Activity) {
            }
        })
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}