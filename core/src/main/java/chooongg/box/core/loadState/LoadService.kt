package chooongg.box.core.loadState

import androidx.annotation.DrawableRes
import androidx.annotation.FloatRange
import chooongg.box.core.loadState.callback.Callback
import chooongg.box.core.loadState.callback.SuccessCallback
import kotlin.reflect.KClass

class LoadService<T>(
    private val convertor: Convertor<T>?,
    val loadLayout: LoadLayout,
    builder: LoadUtils.Builder
) {

    init {
        initCallback(builder)
    }

    private fun initCallback(builder: LoadUtils.Builder) {
        if (builder.callbacks.isNotEmpty()) {
            builder.callbacks.forEach {
                val callback = it.javaObjectType.newInstance()
                loadLayout.setupCallback(callback)
            }
        }

        if (builder.defaultCallback != null) {
            loadLayout.showCallback(builder.defaultCallback!!)
        }
    }

    fun showSuccess() = loadLayout.showCallback(SuccessCallback::class)

    fun showCallback(callback: KClass<out Callback>) = loadLayout.showCallback(callback)

    fun showCallback(
        callback: KClass<out Callback>,
        message: CharSequence?,
        @DrawableRes imageRes: Int? = null
    ) {
        showCallback(callback)
        loadLayout.callbacks[callback]?.onTextChange()?.text = message
        if (imageRes != null) {
            loadLayout.callbacks[callback]?.onImageChange()?.setImageResource(imageRes)
        }
    }

    fun showWithConvertor(t: T) {
        if (convertor == null) throw IllegalArgumentException("You haven't set the Convertor.")
        loadLayout.showCallback(convertor.map(t))
    }

    fun getCurrentCallback() = loadLayout.currentCallback

    fun setCallback(callback: KClass<out Callback>, transport: Transport) = apply {
        loadLayout.setCallback(callback, transport)
    }

    fun setVerticalPercentage(@FloatRange(from = 0.0, to = 1.0) percentage: Float) {
        loadLayout.verticalPercentage = percentage
    }

    fun setHorizontalPercentage(@FloatRange(from = 0.0, to = 1.0) percentage: Float) {
        loadLayout.horizontalPercentage = percentage
    }
}