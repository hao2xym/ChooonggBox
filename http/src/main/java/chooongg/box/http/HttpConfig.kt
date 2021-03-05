package chooongg.box.http

import okhttp3.Interceptor
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

    val networkInterceptor = LinkedList<Interceptor>()


}