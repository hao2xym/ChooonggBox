package chooongg.box.log.handler

import chooongg.box.ext.removeLinefeed
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature
import java.util.regex.Pattern

object AnyLogHandler : LogHandler {

    override fun isHandler(any: Any) = true

    override fun handler(config: LogConfig, any: Any): List<String> {
        return JSON.toJSONString(
            any, SerializerFeature.PrettyFormat, SerializerFeature.DisableCircularReferenceDetect
        ).replace("\t", LogConstant.FORMAT_STEP)
            .removeLinefeed(config.formatter.separator())
            .split(Pattern.compile(config.formatter.separator()))
    }
}