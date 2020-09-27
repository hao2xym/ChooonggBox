package chooongg.box

import android.app.Application

interface IChooonggBoxInitialize {
    fun initialize(application: Application)
    fun onTerminate()
}