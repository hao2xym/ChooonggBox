package chooongg.box.http

import chooongg.box.ext.APP
import chooongg.box.http.logInterceptor.BoxLogInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.fastjson.FastJsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.create
import java.util.concurrent.TimeUnit

object HTTP {

    private var defaultConfig = HttpConfig()

    fun setDefaultConfig(defaultConfig: HttpConfig) {
        this.defaultConfig = defaultConfig
    }

    fun builder(){

    }

    class Builder{

    }


    fun getRetrofit(baseUrl: String) = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClientBuilder().build())
        .addConverterFactory(FastJsonConverterFactory.create())
        .addConverterFactory(ScalarsConverterFactory.create())
        .build().create<API>()

    fun okHttpClientBuilder(config: HttpConfig = defaultConfig) = OkHttpClient.Builder().apply {
        connectTimeout(config.connectTimeout, TimeUnit.SECONDS)
        writeTimeout(config.writeTimeout, TimeUnit.SECONDS)
        readTimeout(config.readTimeout, TimeUnit.SECONDS)
        retryOnConnectionFailure(true)
        cache(Cache(APP.cacheDir, config.cacheSize))
        config.interceptor.forEach { addInterceptor(it) }
        addInterceptor(BoxLogInterceptor)
        config.networkInterceptor.forEach { addNetworkInterceptor(it) }
    }
}