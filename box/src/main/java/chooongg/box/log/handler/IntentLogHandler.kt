package chooongg.box.log.handler

import android.content.Intent
import chooongg.box.log.LogConfig

object IntentLogHandler : LogHandler {
    override fun isHandler(any: Any) = any is Intent

    override fun handler(config: LogConfig, any: Any) = ArrayList<String>().apply {
    }
}