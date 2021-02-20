package chooongg.box

import android.app.Application

internal object BoxRelateLibraryConfig : IBoxInitialize {

    override fun initialize(application: Application) {
        initHttpIfExist(application)
    }

    override fun onTerminate() {
        onTerminateHttpIfExist()
    }

    private fun initHttpIfExist(application: Application) {
        try {
            val clazz = application.classLoader.loadClass("chooongg.box.http.HttpBox")
            val method = clazz.getMethod("initialize", Application::class.java)
            method.invoke(clazz, application)
        } catch (e: Exception) {
        }
    }

    private fun onTerminateHttpIfExist() {
        try {
            val clazz = Class.forName("chooongg.box.http.HttpBox")
            val method = clazz.getMethod("onTerminate")
            method.invoke(null)
        } catch (e: Exception) {
        }
    }

}