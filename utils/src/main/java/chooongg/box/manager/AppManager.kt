package chooongg.box.manager

import android.app.Application
import chooongg.box.ChooonggBoxException

object AppManager {
    private var application: Application? = null

    fun initialize(application: Application) {
        if (this.application != null) throw ChooonggBoxException("你已初始化Application！")
        this.application = application
    }

    fun getApplication() =
        if (application != null) application!! else throw ChooonggBoxException("请初始化chooongg.box.ChooonggBox！")
}