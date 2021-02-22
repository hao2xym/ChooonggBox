package chooongg.box.log.handler

import android.os.Bundle
import chooongg.box.log.formatter.Formatter

object BundleLogHandler : LogHandler {
    override fun isHandler(any: Any) = any is Bundle

    override fun handler(formatter: Formatter, any: Any): List<String> {
        val bundle = any as Bundle
        bundle.keySet().forEach {
            val value = bundle.get(it)
            if (value)
        }
    }
}