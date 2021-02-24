package chooongg.box.log.handler

import chooongg.box.ext.removeLinefeed
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import org.json.JSONArray
import org.json.JSONObject
import java.util.regex.Pattern

object AnyLogHandler : LogHandler {

    override fun isHandler(any: Any) = true

    override fun handler(config: LogConfig, any: Any): List<String> {
        var jsonString = JSON.toJSONString(any, SerializerFeature.DisableCircularReferenceDetect)
            .replace("\\\"", "\"")
        if (jsonString.first() == '\"') jsonString = jsonString.substring(1)
        if (jsonString.last() == '\"') jsonString = jsonString.substringBeforeLast('\"')
        jsonString = when (jsonString.first()) {
            '{' -> JSONObject(jsonString).toString(LogConstant.FORMAT_STEP)
            '[' -> JSONArray(jsonString).toString(LogConstant.FORMAT_STEP)
            else -> jsonString
        }
        return jsonString.removeLinefeed(config.formatter.separator())
            .split(Pattern.compile(config.formatter.separator()))
    }
}