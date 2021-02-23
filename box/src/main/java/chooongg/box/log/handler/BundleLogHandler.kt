package chooongg.box.log.handler

import android.os.Bundle
import chooongg.box.log.LogActuator
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant
import org.json.JSONObject

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
        val step = LogConstant.stepBlank()
        val json = JSONObject()
        keys.forEach { key ->
            LogActuator.handlerLoop(config, bundle.get(key)).forEachIndexed { index, text ->
                when (index) {
                    0 -> add("${step}\"${key}\": $text")
                    config.handlers.size - 1 -> add("${step}${text}")
                    else -> add("${step}${text},")
                }
            }
        }
        add("}")
    }
}