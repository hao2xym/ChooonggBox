package chooongg.box.mmkv

import android.os.Parcelable
import java.lang.reflect.ParameterizedType

open class MMKVKeyParcelable<T : Parcelable?>(
    private val controller: MMKVController,
    val key: String,
    val defaultValue: T
) {
    fun getTClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>

    fun encode(value: T?) = controller.encode(this, value)
    fun decode() = controller.decode(this, defaultValue)
    fun decode(defaultValue: T) = controller.decode(this, defaultValue)
    fun clear() = controller.remove(this)
    fun update(callback: (T) -> T) = encode(callback(decode(defaultValue)))
}