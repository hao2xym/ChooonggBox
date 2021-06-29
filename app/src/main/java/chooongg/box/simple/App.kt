package chooongg.box.simple

import chooongg.box.BoxApplication
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure

class App : BoxApplication() {

    override fun onCreate() {
        super.onCreate()
        if (!isAppMainProcess()) return
        // 友盟
        UMConfigure.setProcessEvent(true)
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }

}