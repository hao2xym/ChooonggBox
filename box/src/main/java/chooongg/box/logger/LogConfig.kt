package chooongg.box.logger

import android.util.Log
import androidx.annotation.IntDef
import androidx.annotation.IntRange
import chooongg.box.Box
import chooongg.box.BuildConfig
import chooongg.box.logger.formatter.DefaultFormatter
import chooongg.box.logger.formatter.Formatter
import chooongg.box.logger.handler.AnyHandler
import chooongg.box.logger.handler.LogHandler
import chooongg.box.logger.printer.LogcatPrinter
import chooongg.box.logger.printer.Printer
import java.util.*

class LogConfig {

    @IntDef(Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR, Log.ASSERT)
    annotation class Level

    var tag = Box.TAG

    /**
     * 启用状态
     */
    var enable = BuildConfig.DEBUG

    /**
     * 默认日志等级
     */
    @Level
    var defaultLevel: Int = Log.DEBUG

    /**
     * 是否显示线程信息
     */
    var showThreadInfo = true

    /**
     * 是否显示堆栈信息
     */
    var showStackInfo = true

    /**
     * 堆栈深度
     */
    @IntRange(from = 1)
    var stackDeep = 1

    /**
     * 堆栈指向偏移
     */
    @IntRange(from = 0L - LogConstant.DEFAULT_STACK_OFFSET)
    var stackOffset = 0

    /**
     * 日志格式化工具
     */
    var formatter: Formatter = DefaultFormatter

    /**
     * 日志处理器
     */
    val handlers = LinkedList<LogHandler<*>>().apply {
        add(AnyHandler)
    }

    /**
     * 日志打印器
     */
    val printers = LinkedList<Printer>().apply {
        add(LogcatPrinter)
    }
}