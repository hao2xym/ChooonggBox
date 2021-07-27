package chooongg.box.log.handler

import chooongg.box.ext.removeLinefeed
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant
import com.google.gson.GsonBuilder
import java.util.regex.Pattern

object AnyLogHandler : LogHandler {

    override fun isHandler(any: Any) = true
    override fun getTypeString(any: Any) = any::class.simpleName
    override fun handler(config: LogConfig, any: Any, columns: Int): List<String> {
        var string = if (any is String) {
            any
        } else GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create().toJson(any)
        string.replace("\t", LogConstant.FORMAT_STEP)
            .removeLinefeed(config.formatter.separator())
        if (columns <= 0) {
            if (string.first() == '\"' && string.last() == '\"') {
                string = string.substring(1)
                string = string.substring(0, string.length - 1)
            }
        }
        return string.split(Pattern.compile(config.formatter.separator()))
    }
}