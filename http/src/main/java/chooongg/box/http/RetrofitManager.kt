package chooongg.box.http

import chooongg.box.ext.APP
import chooongg.box.http.cookie.CookieManager
import chooongg.box.http.logInterceptor.BoxLogInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

object RetrofitManager {

    private var defaultConfig = HttpConfig()

    fun setDefaultConfig(defaultConfig: HttpConfig) {
        this.defaultConfig = defaultConfig
    }

    fun <T : Any> getAPI(clazz: KClass<T>, config: HttpConfig = defaultConfig): T {
        val builder = Retrofit.Builder()
            .baseUrl(getBaseUrlForAnnotation(clazz))
            .client(okHttpClientBuilder(config).build())
        config.converterFactories.forEach { builder.addConverterFactory(it) }
        config.callAdapterFactory.forEach { builder.addCallAdapterFactory(it) }
        val retrofit = builder.build()
        return retrofit.create(clazz.java)
    }

    fun <T : Any> getAPI(clazz: KClass<T>, baseUrl: String, config: HttpConfig = defaultConfig): T {
        val builder = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClientBuilder(config).build())
        config.converterFactories.forEach { builder.addConverterFactory(it) }
        config.callAdapterFactory.forEach { builder.addCallAdapterFactory(it) }
        val retrofit = builder.build()
        return retrofit.create(clazz.java)
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
        }

    private fun <T : Any> getBaseUrlForAnnotation(clazz: KClass<T>): String {
        val findAnnotation = clazz.findAnnotation<BaseUrl>()
            ?: throw RuntimeException("unable to find BaseUrl from Annotation")
        return findAnnotation.value
    }
}