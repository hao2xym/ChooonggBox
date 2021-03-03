package chooongg.box.http.loggerInterceptor

import chooongg.box.log.LogConfig
import okhttp3.Interceptor
import okhttp3.Response

class BoxLogInterceptor(config: ((LogConfig) -> Unit)? = null) : Interceptor {



    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        return chain.proceed(chain.request())
    }
}