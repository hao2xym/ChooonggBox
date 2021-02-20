package chooongg.box

import android.app.Application
import chooongg.box.manager.AppManager

/**
 * ChooonggBox核心初始化类
 */
object Box : IBoxInitialize {
    override fun initialize(application: Application) {
        AppManager.initialize(application)
        BoxRelateLibraryConfig.initialize(application)

    }

    override fun onTerminate() {
        AppManager.onTerminate()
        BoxRelateLibraryConfig.onTerminate()
    }
}