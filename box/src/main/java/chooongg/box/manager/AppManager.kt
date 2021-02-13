package chooongg.box.manager

import android.app.Activity
import android.app.Application
import android.os.Bundle
import chooongg.box.throws.BoxException
import java.util.*

object AppManager : Application.ActivityLifecycleCallbacks {

    private var application: Application? = null

    val activityTask = LinkedList<Activity>()

    val activityTop get() = if (activityTask.isEmpty()) null else activityTask.last

    /**
     * 储存全局Application常量
     */
    fun initialize(application: Application) {
        if (this.application != null) throw BoxException("You have initialize AppManager!")
        application.registerActivityLifecycleCallbacks(this)
        this.application = application
    }

    /**
     * 获取全局Application常量
     */
    fun getApplication() = if (application != null) application!! else
        throw BoxException("Please initialize Box!")

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activityTask.add(activity)
    }

    override fun onActivityDestroyed(activity: Activity) {
        activityTask.remove(activity)
    }

    override fun onActivityStarted(activity: Activity) = Unit
    override fun onActivityResumed(activity: Activity) = Unit
    override fun onActivityPaused(activity: Activity) = Unit
    override fun onActivityStopped(activity: Activity) = Unit
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) = Unit

    fun onTerminate() {
        application?.unregisterActivityLifecycleCallbacks(this)
    }
}