package chooongg.box.log

import android.util.Log
import androidx.annotation.IntDef
import chooongg.box.Box
import chooongg.box.ext.isAppDebug
import chooongg.box.log.formatter.DefaultFormatter
import chooongg.box.log.formatter.Formatter
import chooongg.box.log.printer.LogcatPrinter
import chooongg.box.log.printer.Printer

open class LogConfig {

    @IntDef(Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR, Log.ASSERT)
    annotation class Level

    var tag = Box.TAG

    /**
     * 启用状态
     */
    var enable = isAppDebug()

    /**
     * 默认日志等级
     */
    @Level
    var level: Int = Log.DEBUG

    /**
     * 是否显示线程信息
     */
    var showThreadInfo = isAppDebug()

    /**
     * 是否显示堆栈信息
     */
    var showStackInfo = isAppDebug()

    /**
     * 堆栈深度
     * 最低限制: 1
     */
    var stackDeep = 1

    /**
     * 堆栈指向偏移
     * 最低限制: -LogConstant.DEFAULT_STACK_OFFSET
     */
    var stackOffset = 0

    /**
     * 日志格式化工具
     */
    var formatter: Formatter = DefaultFormatter

    /**
     * 日志处理器
     */
    val handlers = LogConstant.getDefaultHandlers()

    /**
     * 日志打印器
     */
    val printers = LinkedHashSet<Printer>().apply {
        add(LogcatPrinter)
    }
}