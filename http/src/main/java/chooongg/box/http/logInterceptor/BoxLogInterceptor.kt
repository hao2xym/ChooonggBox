package chooongg.box.http.logInterceptor

import chooongg.box.log.LogEntity
import okhttp3.FormBody
import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Response
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

object BoxLogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!BoxHttpLog.config.enable) return chain.proceed(chain.request())
        // Request
        val requestLog = ArrayList<Any?>()
        val request = chain.request()
        requestLog.add(
            LogEntity(
                "[${request.method.toUpperCase(Locale.ROOT)}] ${
                    if (chain.connection() != null) chain.connection()!!.protocol() else ""
                }", buildString {
                    val url = request.url
                    append("host: ${url.host}")
                    append(BoxHttpLog.config.formatter.separator())
                    append("url: $url")
                    if (url.querySize > 0) for (i in 0 until url.querySize) {
                        // TODO
                    }
                }, false
            )
        )
        val requestHeaders = request.headers
        if (BoxHttpLog.config.httpLogLevel == HttpLogLevel.BASIC || BoxHttpLog.config.httpLogLevel == HttpLogLevel.HEADERS) {
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
        if (BoxHttpLog.config.httpLogLevel == HttpLogLevel.BASIC || BoxHttpLog.config.httpLogLevel == HttpLogLevel.BODY) {
            if (requestBody != null) {
                when (requestBody) {
                    is MultipartBody -> {
                        if (requestBody.size > 0) {
                            requestBody.parts.forEachIndexed { index, part ->

                            }
                        }
                    }
                    is FormBody -> {
                        if (requestBody.size > 0) {
                            val keyValue = HashMap<String, String>()
                            for (i in 0 until requestBody.size) {
                                keyValue[requestBody.name(i)] = requestBody.value(i)
                            }
                            requestLog.add(LogEntity("Body", keyValue, false))
                        }
                    }
                    else -> requestBody.isOneShot()
                }
            }
        }
        BoxHttpLog.request(requestLog.toArray())

        // Response
        val currentMillis = System.currentTimeMillis()
        val responseLog = ArrayList<Any?>()
        val response = chain.proceed(request)
        responseLog.add(
            LogEntity(
                "[${request.method.toUpperCase(Locale.ROOT)} ${response.code} ${response.message}]}",
                buildString {
                    append("method: ${request.method}")
                    append(BoxHttpLog.config.formatter.separator())
                    val url = request.url
                    append("host: ${url.host}")
                    append(BoxHttpLog.config.formatter.separator())
                    append("url: $url")
                    append(BoxHttpLog.config.formatter.separator())
                    append("received in: ${currentMillis - response.sentRequestAtMillis}ms")
                },
                false
            )
        )
        val responseHeaders = response.headers
        if (BoxHttpLog.config.httpLogLevel == HttpLogLevel.BASIC || BoxHttpLog.config.httpLogLevel == HttpLogLevel.HEADERS) {
            if (responseHeaders.size > 0) {
                responseLog.add(
                    LogEntity("Headers", buildString {
                        responseHeaders.forEachIndexed { index, pair ->
                            append("${pair.first}: ${pair.second}")
                            if (index != responseHeaders.size - 1) {
                                append(BoxHttpLog.config.formatter.separator())
                            }
                        }
                    }, false)
                )
            }
        }
        val responseBody = response.body

        BoxHttpLog.response(responseLog.toArray())
        return response
    }
}