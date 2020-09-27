package chooongg.box

import android.app.Application
import chooongg.manager.AppManager

object ChooonggBox : IChooonggBoxInitialize {
    override fun initialize(application: Application) {
        AppManager.initialize(application)

    }

    override fun onTerminate() {

    }
}