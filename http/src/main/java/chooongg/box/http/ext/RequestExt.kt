package chooongg.box.http.ext

import chooongg.box.ext.launchIO
import chooongg.box.ext.withMain
import chooongg.box.http.throws.HttpException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import retrofit2.Call

fun <RESPONSE> CoroutineScope.retrofitDefault(dsl: DefaultRetrofitCoroutineDsl<RESPONSE>.() -> Unit): Job {
    val retrofitCoroutineDsl = DefaultRetrofitCoroutineDsl<RESPONSE>()
    retrofitCoroutineDsl.dsl()
    return retrofitCoroutineDsl.request(this)
}

typealias DefaultResponse<T> = Call<T?>

abstract class RetrofitCoroutineDsl<RESPONSE, DATA> {

    lateinit var api: Call<RESPONSE?>

    var body: RESPONSE? = null

    private var onStart: (() -> Unit)? = null

    protected var onResponse: ((RESPONSE) -> Unit)? = null

    protected var onSuccess: ((DATA?) -> Unit)? = null

    private var onFailed: ((HttpException) -> Unit)? = null

    private var onEnd: ((Boolean) -> Unit)? = null

    internal open fun clean() {
        onStart = null
        onResponse = null
        onSuccess = null
        onFailed = null
        onEnd = null
    }

    fun onStart(block: () -> Unit) {
        this.onStart = block
    }

    fun onResponse(block: (RESPONSE) -> Unit) {
        this.onResponse = block
    }

    fun onSuccess(block: (data: DATA?) -> Unit) {
        this.onSuccess = block
    }

    fun configFailed(error: Throwable) {
        onFailed?.invoke(HttpException(error))
    }

    fun onFailed(block: (error: HttpException) -> Unit) {
        this.onFailed = block
    }

    fun onEnd(block: (isSuccess: Boolean) -> Unit) {
        this.onEnd = block
    }

    fun request(coroutineScope: CoroutineScope): Job {
        return coroutineScope.launchIO {
            withMain { onStart?.invoke() }
            val work = async(Dispatchers.IO) {
                try {
                    api.execute()
                } catch (e: Exception) {
                    withMain { configFailed(e) }
                    e.printStackTrace()
                    null
                }
            }
            work.invokeOnCompletion { _ ->
                if (work.isCancelled) {
                    api.cancel()
                    clean()
                }
            }
            try {
                val response = work.await()
                response?.let {
                    if (response.isSuccessful) {
                        val body = response.body()
                        this@RetrofitCoroutineDsl.body = body
                        if (body != null) {
                            withMain {
                                try {
                                    onResponse?.invoke(body)
                                    onEnd?.invoke(true)
                                } catch (e: Exception) {
                                    configFailed(e)
                                    onEnd?.invoke(false)
                                }
                            }
                        } else {
                            withMain {
                                configFailed(HttpException(HttpException.Type.EMPTY))
                                onEnd?.invoke(false)
                            }
                        }
                    } else {
                        withMain {
                            configFailed(HttpException(response.code()))
                            onEnd?.invoke(false)
                        }
                    }
                }
            } catch (e: Exception) {
                withMain {
                    configFailed(e)
                    onEnd?.invoke(false)
                }
            }
        }
    }
}

class DefaultRetrofitCoroutineDsl<RESPONSE> : RetrofitCoroutineDsl<RESPONSE, RESPONSE>() {
    init {
        onResponse = {
            onSuccess?.invoke(it)
        }
    }
}