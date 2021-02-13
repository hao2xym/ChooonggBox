package chooongg.box.mmkv

import java.lang.reflect.ParameterizedType

open class MMKVKey<T>(val key: String, val defaultValue: T) {
    fun getTClass() = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<*>

    fun encode(value: T?) = MMKVUtils.encode(this, value)
    val decode get() = MMKVUtils.decode(this, defaultValue)
    fun decode(defaultValue: T) = MMKVUtils.decode(this, defaultValue)
    fun clear() = MMKVUtils.remove(this)
}