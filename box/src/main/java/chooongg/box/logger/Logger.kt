package chooongg.box.logger

import android.util.Log
import androidx.annotation.IntDef
import chooongg.box.logger.formatter.DefaultFormatter
import chooongg.box.logger.formatter.Formatter
import chooongg.box.logger.handler.LoggerHandler
import chooongg.box.logger.printer.LogcatPrinter
import chooongg.box.logger.printer.LoggerPrinter
import java.util.*

object Logger {

    @IntDef(Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR, Log.ASSERT)
    annotation class LogLevel

    private val formatter: Formatter = DefaultFormatter

    private val printers = LinkedList<LoggerPrinter>()
    private val defaultPrinter = LinkedList<LoggerPrinter>().apply {
        add(LogcatPrinter(formatter))
    }

    private val handlers = LinkedList<LoggerHandler<*>>()
    private val defaultHandler = LinkedList<LoggerHandler<*>>().apply {

    }

    init {
        handlers
    }

}