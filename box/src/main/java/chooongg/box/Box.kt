package chooongg.box

import android.app.Application
import chooongg.box.ext.initNightMode
import chooongg.box.manager.AppManager
import com.tencent.mmkv.MMKV

/**
 * ChooonggBox核心初始化类
 */
object Box : IBoxInitialize {

    const val TAG = "ChooonggBox"

    override fun initialize(application: Application) {
        MMKV.initialize(application)
        application.initNightMode()
        AppManager.initialize(application)
        BoxLibrariesConfig.initialize(application)
    }
}