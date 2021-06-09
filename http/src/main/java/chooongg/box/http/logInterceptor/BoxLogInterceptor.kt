package chooongg.box.http.logInterceptor

import chooongg.box.log.LogConstant
import chooongg.box.log.LogEntity
import okhttp3.*
import okhttp3.internal.http.promisesBody
import okio.Buffer
import okio.EOFException
import okio.GzipSource
import okio.IOException
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

object BoxLogInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (!BoxHttpLog.config.enable) return chain.proceed(chain.request())
        val startNs = System.nanoTime()
        // Request
        val request = chain.request()
        printlnRequestLog(request)

        val response = chain.proceed(request)

        // Response
        val receivedMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)
        printlnResponseLog(receivedMs, response, request)
        return response
    }

    private fun printlnRequestLog(request: Request) {
        val requestLog = ArrayList<Any?>()
        requestLog.add(
            LogEntity(
                "[${request.method.uppercase(Locale.ROOT)}]", buildString {
                    val url = request.url
                    append("host: ${url.host}")
                    append(BoxHttpLog.config.formatter.separator())
                    append("url: $url")
                    if (url.querySize > 1) {
                        for (i in 0 until url.querySize) {
                            append(BoxHttpLog.config.formatter.separator())
                            when (i) {
                                0 -> append(LogConstant.TABS_SL_ST)
                                url.querySize - 1 -> append(LogConstant.TABS_SL_SB)
                                else -> append(LogConstant.TABS_SL_SM)
                            }
                            append(LogConstant.BLANK)
                                .append(url.queryParameterName(0)).append(": ")
                                .append(url.queryParameterValue(0))
                        }
                    } else if (url.querySize > 0) {
                        append(BoxHttpLog.config.formatter.separator())
                        append(LogConstant.TABS_SL_SM).append(LogConstant.BLANK)
                            .append(url.queryParameterName(0)).append(": ")
                            .append(url.queryParameterValue(0))
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
                bodyToString(requestBody, request.headers)
            }
        }
        BoxHttpLog.request(requestLog.toArray())
    }

    private fun printlnResponseLog(receivedMs: Long, response: Response, request: Request) {
        val responseLog = ArrayList<Any?>()
        responseLog.add(
            LogEntity(
                "[${request.method.uppercase(Locale.ROOT)} ${response.code} ${response.message}]}",
                buildString {
                    append("method: ${request.method}")
                    append(BoxHttpLog.config.formatter.separator())
                    val url = request.url
                    append("host: ${url.host}")
                    append(BoxHttpLog.config.formatter.separator())
                    append("url: $url")
                    append(BoxHttpLog.config.formatter.separator())
                    append("state code: ${receivedMs}ms")
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
                            if (index == 0) append(BoxHttpLog.config.formatter.separator())
                            append("${pair.first}: ${pair.second}")
                        }
                    }, false)
                )
            }
        }
        val responseBody = response.body

        BoxHttpLog.response(responseLog.toArray())
    }

    private fun getResponseBody(response: Response): String {
        val responseBody = response.body!!
        val headers = response.headers
        val contentLength = responseBody.contentLength()
        if (!response.promisesBody()) {
            return "End request - Promises Body"
        } else if (bodyHasUnknownEncoding(response.headers)) {
            return "encoded body omitted"
        } else {
            val source = responseBody.source()
            source.request(Long.MAX_VALUE) // Buffer the entire body.
            var buffer = source.buffer

            var gzippedLength: Long? = null
            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                gzippedLength = buffer.size
                GzipSource(buffer.clone()).use { gzippedResponseBody ->
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                }
            }

            val contentType = responseBody.contentType()
            val charset: Charset = contentType?.charset(StandardCharsets.UTF_8)
                ?: StandardCharsets.UTF_8

            if (!buffer.isProbablyUtf8()) {
                return "End request - binary ${buffer.size}:byte body omitted"
            }

            if (contentLength != 0L) {
                return getJsonString(buffer.clone().readString(charset))
            }

            return if (gzippedLength != null) {
                "End request - ${buffer.size}:byte, $gzippedLength-gzipped-byte body"
            } else {
                "End request - ${buffer.size}:byte body"
            }
        }
    }

    private fun bodyToString(requestBody: RequestBody, headers: Headers): String {
        return try {
            when {
                bodyHasUnknownEncoding(headers) -> {
                    return "encoded body omitted)"
                }
                requestBody.isDuplex() -> {
                    return "duplex request body omitted"
                }
                requestBody.isOneShot() -> {
                    return "one-shot body omitted"
                }
                else -> {
                    val buffer = Buffer()
                    requestBody.writeTo(buffer)

                    val contentType = requestBody.contentType()
                    val charset: Charset = contentType?.charset(StandardCharsets.UTF_8)
                        ?: StandardCharsets.UTF_8

                    return if (buffer.isProbablyUtf8()) {
                        getJsonString(buffer.readString(charset)) + BoxHttpLog.config.formatter.separator() + "${requestBody.contentLength()}-byte body"
                    } else {
                        "binary ${requestBody.contentLength()}-byte body omitted"
                    }
                }
            }
        } catch (e: IOException) {
            "{\"err\": \"" + e.message + "\"}"
        }
    }

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers["Content-Encoding"] ?: return false
        return !contentEncoding.equals("identity", ignoreCase = true) &&
                !contentEncoding.equals("gzip", ignoreCase = true)
    }

    private fun getJsonString(msg: String): String {
        return try {
            when {
                msg.startsWith("{") -> {
                    val jsonObject = JSONObject(msg)
                    jsonObject.toString(LogConstant.FORMAT_STEP_COUNT)
                }
                msg.startsWith("[") -> {
                    val jsonArray = JSONArray(msg)
                    jsonArray.toString(LogConstant.FORMAT_STEP_COUNT)
                }
                else -> {
                    msg
                }
            }
        } catch (e: JSONException) {
            msg
        } catch (e1: OutOfMemoryError) {
            msg
        }
    }

    private fun Buffer.isProbablyUtf8(): Boolean {
        try {
            val prefix = Buffer()
            val byteCount = size.coerceAtMost(64)
            copyTo(prefix, 0, byteCount)
            for (i in 0 until 16) {
                if (prefix.exhausted()) {
                    break
                }
                val codePoint = prefix.readUtf8CodePoint()
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false
                }
            }
            return true
        } catch (_: EOFException) {
            return false // Truncated UTF-8 sequence.
        }
    }
}