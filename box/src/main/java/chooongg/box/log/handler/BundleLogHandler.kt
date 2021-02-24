package chooongg.box.log.handler

import android.os.Bundle
import chooongg.box.log.LogActuator
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant

object BundleLogHandler : LogHandler {

    override fun isHandler(any: Any) = any is Bundle

    override fun handler(config: LogConfig, any: Any) = ArrayList<String>().apply {
        val bundle = any as Bundle
        val keys = bundle.keySet()
        if (keys.isEmpty()) {
            add("{}")
            return@apply
        }
        add("{")
        keys.forEachIndexed { index, key ->
            val lastPunctuation = if (index == keys.size - 1) "" else ","
            val values = LogActuator.handlerLoop(config, bundle.get(key))
            when {
                values.isEmpty() -> add("${LogConstant.FORMAT_STEP}\"${key}\":null${lastPunctuation}")
                values.size == 1 -> add("${LogConstant.FORMAT_STEP}\"${key}\":${values[0]}${lastPunctuation}")
                else -> values.forEachIndexed { childIndex, text ->
                    when (childIndex) {
                        0 -> add("${LogConstant.FORMAT_STEP}\"${key}\":$text")
                        values.size - 1 -> add("${LogConstant.FORMAT_STEP}$text${lastPunctuation}")
                        else -> add("${LogConstant.FORMAT_STEP}$text")
                    }
                }
            }
        }
        add("}")
    }
}