package chooongg.box

import android.app.Application
import android.content.Context
import android.os.Process
import chooongg.box.ext.activityManager

open class BoxApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        if (!isAppMainProcess()) return
        Box.initialize(this)
    }

    /**
     * 判断是不是在主进程中
     * 根据判断可防止多进程时多次初始化问题
     */
    protected fun isAppMainProcess() = getCurrentProcessName(this).equals(packageName)

    /**
     * 获取当前进程名称
     */
    protected fun getCurrentProcessName(context: Context): String? {
        val pid = Process.myPid()
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) return appProcess.processName
        }
        return null
    }
}