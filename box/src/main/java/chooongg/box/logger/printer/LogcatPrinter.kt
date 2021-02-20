package chooongg.box.logger.printer

import android.util.Log
import chooongg.box.logger.Logger
import chooongg.box.logger.formatter.Formatter

class LogcatPrinter(val formatter: Formatter) : LoggerPrinter {

    override fun printLog(@Logger.LogLevel logLevel: Int, tag: String, msg: String) {
        when (logLevel) {
            Log.VERBOSE -> Log.v(tag, msg)
            Log.DEBUG -> Log.d(tag, msg)
            Log.INFO -> Log.i(tag, msg)
            Log.WARN -> Log.w(tag, msg)
            Log.ERROR -> Log.e(tag, msg)
            Log.ASSERT -> Log.wtf(tag, msg)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is LogcatPrinter && other.formatter == formatter) return true
        return false
    }

    override fun hashCode(): Int {
        return formatter.hashCode()
    }
}