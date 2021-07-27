package chooongg.box.log.handler

import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant
import org.json.JSONArray
import org.json.JSONObject

object JsonLogHandler : LogHandler {
    override fun isHandler(any: Any): Boolean {
        if (any is String || any is CharSequence) {
            val str = any.toString()
            if (str.isBlank()) return false
            try {
                JSONObject(str)
                return true
            } catch (e: Exception) {
            }
            try {
                JSONArray(str)
                return true
            } catch (e: Exception) {
            }
        }
        return false
    }

    override fun getTypeString(any: Any) = "Json"

    override fun handler(config: LogConfig, any: Any, columns: Int): List<String> {
        val string = any.toString()
        try {
            return JSONObject(string).toString(LogConstant.FORMAT_STEP_COUNT).split(Regex("[\r\n]"))
        } catch (e: Exception) {
        }
        try {
            return JSONArray(string).toString(LogConstant.FORMAT_STEP_COUNT).split(Regex("[\r\n]"))
        } catch (e: Exception) {
        }
        return listOf(string)
    }
}