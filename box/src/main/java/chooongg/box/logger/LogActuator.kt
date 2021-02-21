package chooongg.box.logger

import java.util.*
import kotlin.math.min

object LogActuator {

    fun log(
        config: LogConfig,
        @LogConfig.Level level: Int?,
        tag: String?,
        childTag: String?,
        vararg any: Any?
    ) {
        if (!config.enable) return
        val log = StringBuilder()

        // ThreadInfo
        log.append(
            config.formatter.top(
                childTag, if (config.showThreadInfo) {
                    "[Thread: ${Thread.currentThread().name}]"
                } else {
                    null
                }
            )
        )

        // StackInfo
        if (config.showStackInfo) {
            val stackTrace = Throwable().stackTrace
            val stackIndex = LogConstant.DEFAULT_STACK_OFFSET + config.stackOffset
            if (stackIndex < stackTrace.size) {
                var targetElement = stackTrace[LogConstant.DEFAULT_STACK_OFFSET]
                val fileName = getFileName(targetElement)
                val head = Formatter()
                    .format(
                        "%s.%s(%s:%d)",
                        targetElement.className,
                        targetElement.methodName,
                        fileName,
                        targetElement.lineNumber
                    )
                    .toString()
                if (config.stackDeep <= 1) {
                    log.append(config.formatter.separator()).append(config.formatter.middle(head))
                }else {
                    val consoleHead = arrayOfNulls<String>(
                        min(config.stackDeep, stackTrace.size - stackIndex)
                    )
                    var i = 0
                    val len = consoleHead.size
                    while (i < len) {
                        targetElement = stackTrace[i + stackIndex]
                        consoleHead[i] = Formatter()
                            .format(
                                "%s.%s(%s:%d)",
                                targetElement.className,
                                targetElement.methodName,
                                getFileName(targetElement),
                                targetElement.lineNumber
                            )
                            .toString()
                        ++i
                    }
                    consoleHead.forEach {
                        log.append(config.formatter.separator()).append(config.formatter.middle(it))
                    }
                }
            }
        }

        // Content


        // End
        log.append(config.formatter.separator()).append(config.formatter.bottom())

        config.printers.forEach {
            it.printLog(level ?: config.defaultLevel, tag ?: config.tag, log.toString())
        }
    }

    private fun getFileName(targetElement: StackTraceElement): String {
        val fileName = targetElement.fileName
        if (fileName != null) return fileName
        // If name of file is null, should add
        // "-keepattributes SourceFile,LineNumberTable" in proguard file.
        var className = targetElement.className
        val classNameInfo =
            className.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        if (classNameInfo.isNotEmpty()) {
            className = classNameInfo[classNameInfo.size - 1]
        }
        val index = className.indexOf('$')
        if (index != -1) {
            className = className.substring(0, index)
        }
        return "$className.java"
    }
}