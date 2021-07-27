package chooongg.box.http.ext

import chooongg.box.ext.withIO
import chooongg.box.ext.withMain
import chooongg.box.http.throws.HttpException

interface ResponseData<DATA> {
    suspend fun checkData(): DATA?
}

@Suppress("DEPRECATION")
suspend fun <RESPONSE : ResponseData<DATA>, DATA> requestSimple(block: RetrofitCoroutinesSimpleDsl<RESPONSE, DATA>.() -> Unit) {
    val dsl = RetrofitCoroutinesSimpleDsl<RESPONSE, DATA>()
    block.invoke(dsl)
    dsl.executeRequest()
}

@Suppress("DEPRECATION")
suspend fun <RESPONSE> requestDefault(block: RetrofitCoroutinesDefaultDsl<RESPONSE>.() -> Unit) {
    val dsl = RetrofitCoroutinesDefaultDsl<RESPONSE>()
    block.invoke(dsl)
    dsl.executeRequest()
}

open class RetrofitCoroutinesSimpleDsl<RESPONSE : ResponseData<DATA>, DATA> :
    RetrofitCoroutinesDefaultDsl<RESPONSE>() {

    protected var onSuccess: (suspend (DATA) -> Unit)? = null

    fun onSuccess(block: suspend (data: DATA) -> Unit) {
        this.onSuccess = block
    }

    override suspend fun processData(response: RESPONSE) {
        val data = response.checkData() ?: throw HttpException(HttpException.Type.EMPTY)
        withMain { onSuccess?.invoke(data) }
    }
}

open class RetrofitCoroutinesDefaultDsl<RESPONSE> {

    private var api: (suspend () -> RESPONSE)? = null

    private var onStart: (suspend () -> Unit)? = null

    private var onResponse: (suspend (RESPONSE) -> Unit)? = null

    private var onFailed: (suspend (HttpException) -> Unit)? = null

    private var onEnd: (suspend (Boolean) -> Unit)? = null

    fun api(block: suspend () -> RESPONSE) {
        this.api = block
    }

    fun onStart(block: suspend () -> Unit) {
        this.onStart = block
    }

    fun onResponse(block: suspend (RESPONSE) -> Unit) {
        this.onResponse = block
    }

    fun onFailed(block: suspend (error: HttpException) -> Unit) {
        this.onFailed = block
    }

    fun onEnd(block: suspend (isSuccess: Boolean) -> Unit) {
        this.onEnd = block
    }

    protected open suspend fun processData(response: RESPONSE) = Unit

    @Suppress("ThrowableNotThrown")
    @Deprecated("Calling this method will result in repeated calls, but you can call it when encapsulating the tool method")
    suspend fun executeRequest() {
        if (api == null) return
        withMain { onStart?.invoke() }
        withIO {
            try {
                val response = api!!.invoke()
                withMain { onResponse?.invoke(response) }
                processData(response)
                withMain { onEnd?.invoke(true) }
            } catch (e: Throwable) {
                withMain { onFailed?.invoke(HttpException(e)) }
                withMain { onEnd?.invoke(false) }
            }
        }
    }
}