package chooongg.box.mmkv

import android.os.Parcelable
import java.lang.reflect.ParameterizedType

open class MMKVKeyParcelable<T : Parcelable?>(val key: String, val defaultValue: T) {
    fun getTClass() =
        (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>

    fun put(value: T?) = MMKVController.put(this, value)
    val get get() = MMKVController.get(this, defaultValue)
    fun get(defaultValue: T) = MMKVController.get(this, defaultValue)
    fun clear() = MMKVController.remove(this)
    fun update(callback: (T) -> T) = put(callback(get(defaultValue)))
}