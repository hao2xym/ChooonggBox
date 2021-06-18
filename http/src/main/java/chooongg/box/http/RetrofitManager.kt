package chooongg.box.http

import chooongg.box.ext.APP
import chooongg.box.http.cookie.CookieManager
import chooongg.box.http.logInterceptor.BoxLogInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

object RetrofitManager {

    private var defaultConfig = HttpConfig()

    fun setDefaultConfig(defaultConfig: HttpConfig) {
        this.defaultConfig = defaultConfig
    }

    fun <T> getAPI(
        clazz: Class<T>,
        baseUrl: String? = null,
        config: HttpConfig = defaultConfig
    ): T {
        val builder = Retrofit.Builder()
            .client(okHttpClientBuilder(config).build())
        if (!baseUrl.isNullOrEmpty()) builder.baseUrl(baseUrl)
        config.converterFactories.forEach { builder.addConverterFactory(it) }
        config.callAdapterFactory.forEach { builder.addCallAdapterFactory(it) }
        config.retrofitBuilder?.invoke(builder)
        val retrofit = builder.build()
        return retrofit.create(clazz)
    }

    private fun okHttpClientBuilder(config: HttpConfig = defaultConfig) =
        OkHttpClient.Builder().apply {
            connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
            writeTimeout(config.writeTimeout, TimeUnit.SECONDS)
            readTimeout(config.readTimeout, TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            cache(Cache(APP.cacheDir, config.cacheSize))
            cookieJar(CookieManager(APP))
            config.interceptor.forEach { addInterceptor(it) }
            addInterceptor(BoxLogInterceptor)
            config.networkInterceptor.forEach { addNetworkInterceptor(it) }
            config.okHttpClientBuilder?.invoke(this)
        }

    private fun <T> getBaseUrlForAnnotation(clazz: Class<T>): String {
        if (clazz.javaClass.isAnnotationPresent(BaseUrl::class.java)) {
            return clazz.javaClass.getAnnotation(BaseUrl::class.java).value
        } else throw RuntimeException("unable to find BaseUrl from Annotation")
    }
}