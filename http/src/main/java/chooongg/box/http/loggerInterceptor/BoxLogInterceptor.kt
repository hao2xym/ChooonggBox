package chooongg.box.http.loggerInterceptor

import chooongg.box.log.LogEntity
import okhttp3.Interceptor
import okhttp3.Response

class BoxLogInterceptor(config: ((HttpLogConfig) -> Unit)? = null) : Interceptor {

    init {
        if (config != null) BoxHttpLog.config(config)
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        // Request
        val requestLog = ArrayList<Any?>()
        val request = chain.request()
        requestLog.add(
            LogEntity(
                "[${request.method}] ${
                    if (chain.connection() != null) chain.connection()!!.protocol() else ""
                }", buildString {
                    append("method: ${request.method}")
                    append(BoxHttpLog.config.formatter.separator())
                    val url = request.url
                    append("url: $url")
                    append(BoxHttpLog.config.formatter.separator())
                    append("host: ${url.host}")
                }, false
            )
        )

        val requestHeaders = request.headers
        if (BoxHttpLog.config.type == HttpTypeLevel.BASIC || BoxHttpLog.config.type == HttpTypeLevel.HEADERS) {
            if (requestHeaders.size > 0) {
                requestLog.add(
                    LogEntity("Headers", buildString {
                        requestHeaders.forEachIndexed { index, pair ->
                            append("${pair.first}: ${pair.second}")
                            if (index != requestHeaders.size - 1) {
                                append(BoxHttpLog.config.formatter.separator())
                            }
                        }
                    }, false)
                )
            }
        }

        val requestBody = request.body
        if (BoxHttpLog.config.type == HttpTypeLevel.BASIC || BoxHttpLog.config.type == HttpTypeLevel.BODY) {
            if (requestBody!= null){
                requestBody
            }
        }


        BoxHttpLog.request(requestLog.toArray())

        // Response
        val response = chain.proceed(request)

        return response
    }
}