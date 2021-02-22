package chooongg.box.log.handler

import chooongg.box.ext.removeLinefeed
import chooongg.box.log.formatter.Formatter
import java.util.regex.Pattern

object AnyLogHandler : LogHandler {
    override fun getChildTag(any: Any): String? =
        if (any is CharSequence) null else any::class.simpleName

    override fun isHandler(any: Any) = true
    override fun handler(formatter: Formatter, any: Any) =
        any.toString()
            .removeLinefeed(formatter.separator())
            .split(Pattern.compile(formatter.separator()))
}