package chooongg.box

import android.app.Application

interface IBoxInitialize {
    fun initialize(application: Application)
    fun onTerminate()
}