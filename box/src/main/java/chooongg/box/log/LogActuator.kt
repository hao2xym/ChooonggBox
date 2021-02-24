package chooongg.box.log

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
                } else {
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
        any.forEachIndexed { index, item ->
            val contentTag: String?
            val content: Any?
            when {
                item is LogBean -> {
                    contentTag = "${item.childTag}~${item::class.simpleName}~"
                    content = item.any
                }
                item != null -> {
                    contentTag = "~${item::class.simpleName}~"
                    content = item
                }
                else -> {
                    contentTag = null
                    content = item
                }
            }
            if (content == null) {
                log.append(config.formatter.separator())
                    .append(config.formatter.middle(LogConstant.NONE))
            } else {
                log.append(config.formatter.separator())
                if (index == 0) {
                    log.append(config.formatter.middlePrimary(contentTag))
                } else {
                    log.append(config.formatter.middleSecondary(contentTag))
                }

                handlerLoop(config, content).forEach { text ->
                    log.append(config.formatter.separator())
                        .append(config.formatter.middle(text))
                }
            }
        }

        // End
        log.append(config.formatter.separator()).append(config.formatter.bottom())

        config.printers.forEach {
            it.printLog(level ?: config.defaultLevel, tag ?: config.tag, log.toString())
        }
    }

    fun handlerLoop(config: LogConfig, any: Any?): List<String> = ArrayList<String>().apply {
        if (any == null) {
            add(LogConstant.NONE)
            return@apply
        }
        config.handlers.reversed().forEach { handler ->
            if (handler.isHandler(any)) {
                addAll(handler.handler(config, any))
                return@apply
            }
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