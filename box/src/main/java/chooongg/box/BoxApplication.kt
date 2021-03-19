package chooongg.box

import android.app.Application
import android.os.Process
import androidx.appcompat.app.AppCompatDelegate
import chooongg.box.ext.activityManager

open class BoxApplication : Application() {

    companion object {
        init {
            // 兼容矢量图片
            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (!isAppMainProcess()) return
        Box.initialize(this)
    }

    /**
     * 判断是不是在主进程中
     * 根据判断可防止多进程时多次初始化问题
     */
    protected fun isAppMainProcess() = getCurrentProcessName().equals(packageName)

    /**
     * 获取当前进程名称
     */
    protected fun getCurrentProcessName(): String? {
        val pid = Process.myPid()
        for (appProcess in activityManager.runningAppProcesses) {
            if (appProcess.pid == pid) return appProcess.processName
        }
        return null
    }
}