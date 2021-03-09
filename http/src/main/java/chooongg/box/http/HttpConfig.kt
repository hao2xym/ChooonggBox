package chooongg.box.http

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.fastjson.FastJsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.*

open class HttpConfig {

    /**
     * 缓存保存时间-秒
     */
    var maxStale = 60 * 60 * 4 * 28

    /**
     * 缓存大小
     */
    var cacheSize = BoxHttp.DEFAULT_HTTP_CACHE_SIZE

    /**
     * 超时时间-秒
     */
    var connectTimeout = BoxHttp.DEFAULT_TIMEOUT
    var writeTimeout = BoxHttp.DEFAULT_TIMEOUT
    var readTimeout = BoxHttp.DEFAULT_TIMEOUT

    /**
     * 拦截器
     */
    val interceptor = LinkedList<Interceptor>()

    /**
     * 网络层拦截器
     */
    val networkInterceptor = LinkedList<Interceptor>()

    val converterFactories = arrayListOf<Converter.Factory>(
        FastJsonConverterFactory.create(),
        ScalarsConverterFactory.create()
    )

    val callAdapterFactory = arrayListOf<CallAdapter.Factory>()

    /**
     * 扩展方法，可以进行OkHttpClient的特殊配置
     */
    val okHttpClientBuilder: ((OkHttpClient.Builder) -> Unit)? = null

    /**
     * 扩展方法，可以进行Retrofit的特殊配置
     */
    val retrofitBuilder: ((Retrofit.Builder) -> Unit)? = null
}