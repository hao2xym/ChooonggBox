package chooongg.box.mmkv

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import java.lang.reflect.ParameterizedType

object MMKVController {

    private val mmkv: MMKV by lazy {
        MMKV.defaultMMKV()
    }

    fun <T> put(mmkvKey: MMKVKey<T>, value: T?) {
        if (value == null) mmkv.removeValueForKey(mmkvKey.key)
        else when (mmkvKey.getTClass()) {
            Int::class.javaObjectType -> mmkv.encode(mmkvKey.key, value as Int)
            Long::class.javaObjectType -> mmkv.encode(mmkvKey.key, value as Long)
            Float::class.javaObjectType -> mmkv.encode(mmkvKey.key, value as Float)
            Double::class.javaObjectType -> mmkv.encode(mmkvKey.key, value as Double)
            String::class.javaObjectType -> mmkv.encode(mmkvKey.key, value as String)
            Boolean::class.javaObjectType -> mmkv.encode(mmkvKey.key, value as Boolean)
            ByteArray::class.javaObjectType -> mmkv.encode(mmkvKey.key, value as ByteArray)
            else -> throw RuntimeException("MMKV can't support the data type(${mmkvKey.getTClass().name})")
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(mmkvKey: MMKVKey<T>, defaultValue: T = mmkvKey.defaultValue): T {
        return if (mmkv.containsKey(mmkvKey.key)) {
            when (mmkvKey.getTClass()) {
                Int::class.javaObjectType -> mmkv.decodeInt(mmkvKey.key) as T
                Long::class.javaObjectType -> mmkv.decodeLong(mmkvKey.key) as T
                Float::class.javaObjectType -> mmkv.decodeFloat(mmkvKey.key) as T
                Double::class.javaObjectType -> mmkv.decodeDouble(mmkvKey.key) as T
                String::class.javaObjectType -> mmkv.decodeString(mmkvKey.key) as T
                Boolean::class.javaObjectType -> mmkv.decodeBool(mmkvKey.key) as T
                ByteArray::class.javaObjectType -> mmkv.decodeBytes(mmkvKey.key) as T
                else -> throw RuntimeException("MMKV can't support the data type(${mmkvKey.getTClass().name})")
            }
        } else defaultValue
    }

    fun <T : Parcelable?> put(mmkvKey: MMKVKeyParcelable<T>, value: T?) {
        if (value == null) mmkv.removeValueForKey(mmkvKey.key)
        else mmkv.encode(mmkvKey.key, value)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Parcelable?> get(
        mmkvKey: MMKVKeyParcelable<T>,
        defaultValue: T = mmkvKey.defaultValue
    ): T {
        return if (mmkv.containsKey(mmkvKey.key)) mmkv.decodeParcelable(
            mmkvKey.key,
            mmkvKey.getTClass() as Class<Parcelable>
        ) as T
        else defaultValue
    }

    fun remove(vararg mmkvKey: MMKVKey<*>) {
        if (mmkvKey.isNotEmpty()) {
            val keys = Array(mmkvKey.size) {
                mmkvKey[it].key
            }
            mmkv.removeValuesForKeys(keys)
        }
    }

    fun remove(vararg mmkvKeyParcelable: MMKVKeyParcelable<*>) {
        if (mmkvKeyParcelable.isNotEmpty()) {
            val keys = Array(mmkvKeyParcelable.size) {
                mmkvKeyParcelable[it].key
            }
            mmkv.removeValuesForKeys(keys)
        }
    }
}