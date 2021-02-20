package chooongg.box

import android.app.Application

internal object BoxRelateLibraryConfig {

    fun initialize(application: Application) {
        initHttpIfExist(application)
    }

    private fun initCoreIfExist(application: Application) {
        try {
            val clazz = application.classLoader.loadClass("chooongg.box.core.HttpBox")
            val method = clazz.getMethod("init", Application::class.java)
            method.invoke(clazz, application)
        } catch (e: Exception) {
        }
    }

    private fun initHttpIfExist(application: Application) {
        try {
            val clazz = application.classLoader.loadClass("chooongg.box.http.HttpBox")
            val method = clazz.getMethod("init", Application::class.java)
            method.invoke(clazz, application)
        } catch (e: Exception) {
        }
    }
}