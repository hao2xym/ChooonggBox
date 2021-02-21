package chooongg.box

import android.app.Application

internal object BoxRelateLibraryConfig : IBoxInitialize {

    override fun initialize(application: Application) {
        initializeCoreIfExist(application)
        initializeHttpIfExist(application)
    }

    /**
     * BoxCore
     */
    private fun initializeCoreIfExist(application: Application) {
        try {
            val clazz = application.classLoader.loadClass("chooongg.box.core.BoxCore")
            val method = clazz.getMethod("initialize", Application::class.java)
            method.invoke(clazz, application)
        } catch (e: Exception) {
        }
    }

    /**
     * BoxHttp
     */
    private fun initializeHttpIfExist(application: Application) {
        try {
            val clazz = application.classLoader.loadClass("chooongg.box.http.BoxHttp")
            val method = clazz.getMethod("initialize", Application::class.java)
            method.invoke(clazz, application)
        } catch (e: Exception) {
        }
    }
}