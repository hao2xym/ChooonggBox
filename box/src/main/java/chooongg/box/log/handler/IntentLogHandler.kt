package chooongg.box.log.handler

import android.content.Intent
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant

object IntentLogHandler : LogHandler {
    override fun isHandler(any: Any) = any is Intent

    override fun handler(config: LogConfig, any: Any) = ArrayList<String>().apply {
        val intent = any as Intent
        add("{")
        add("${LogConstant.FORMAT_STEP}Scheme:${intent.scheme ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}action:${intent.action ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}DataString:${intent.dataString ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}Type:${intent.type ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}Package:${intent.`package` ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}Categories:${intent.categories ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}action:${intent.action ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}action:${intent.action ?: LogConstant.NONE}")
        add("${LogConstant.FORMAT_STEP}action:${intent.action ?: LogConstant.NONE}")
        add("}")
    }
}