package chooongg.box

import android.app.Application
import chooongg.box.manager.AppManager

object ChooonggBox : IChooonggBoxInitialize {
    override fun initialize(application: Application) {
        AppManager.initialize(application)

    }

    override fun onTerminate() {

    }
}