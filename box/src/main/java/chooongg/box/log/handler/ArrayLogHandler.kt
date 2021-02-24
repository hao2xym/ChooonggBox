package chooongg.box.log.handler

import chooongg.box.log.LogActuator
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant

object ArrayLogHandler : LogHandler {

    override fun isHandler(any: Any) = any is Array<*>
            || any is IntArray
            || any is ByteArray
            || any is CharArray
            || any is LongArray
            || any is FloatArray
            || any is ShortArray
            || any is DoubleArray
            || any is BooleanArray

    override fun handler(config: LogConfig, any: Any) = ArrayList<String>().apply {
        val array: Array<*> = when (any) {
            is Array<*> -> any
            is IntArray -> any.toTypedArray()
            is ByteArray -> any.toTypedArray()
            is CharArray -> any.toTypedArray()
            is LongArray -> any.toTypedArray()
            is FloatArray -> any.toTypedArray()
            is ShortArray -> any.toTypedArray()
            is DoubleArray -> any.toTypedArray()
            is BooleanArray -> any.toTypedArray()
            else -> arrayOf(any)
        }
        val step = LogConstant.stepBlank()
        if (array.isEmpty()) {
            add("[]")
            return@apply
        }
        add("[")
        array.toList()
        array.forEach { item ->
            LogActuator.handlerLoop(config, item).forEach {
                add("${step}${it}")
            }
        }
        add("]")
    }
}