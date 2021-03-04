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
        val threadInfo =
            if (config.showThreadInfo) "[Thread: ${Thread.currentThread().name}]" else null

        // StackInfo
        if (config.showStackInfo) {
            log.append(config.formatter.top(threadInfo, childTag, null))

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
            val isShowType: Boolean
            if (item is LogEntity) {
                contentTag = "${item.contentTag}"
                content = item.any
                isShowType = item.isShowType
            } else {
                contentTag = null
                content = item
                isShowType = true
            }
            if (content == null) {
                log.append(config.formatter.separator())
                    .append(config.formatter.middle(LogConstant.NONE))
            } else {
                var classType: String? = null
                val contentSplit = ArrayList<String>().apply {
                    config.handlers.reversed().forEach { handler ->
                        if (handler.isHandler(any)) {
                            classType = handler.getTypeString(any)
                            addAll(handler.handler(config, any, 0))
                            return@apply
                        }
                    }
                }

                val contentClassType = if (isShowType) classType else null
                log.append(config.formatter.separator())
                if (index == 0) {
                    if (config.showStackInfo) {
                        log.append(config.formatter.middlePrimary(contentTag, contentClassType))
                    } else {
                        val tempTag = if (childTag != null && contentTag != null) {
                            "$childTag/$contentTag"
                        } else childTag ?: contentTag
                        log.append(config.formatter.top(threadInfo, tempTag, contentClassType))
                    }
                } else {
                    log.append(config.formatter.middleSecondary(contentTag, contentClassType))
                }

                contentSplit.forEach { text ->
                    log.append(config.formatter.separator())
                        .append(config.formatter.middle(text))
                }
            }
        }

        // End
        log.append(config.formatter.separator()).append(config.formatter.bottom())

        config.printers.forEach {
            it.printLog(level ?: config.level, tag ?: config.tag, log.toString())
        }
    }

    fun handlerLoop(config: LogConfig, any: Any?, columns: Int): List<String> =
        ArrayList<String>().apply {
            if (any == null) {
                add(LogConstant.NONE)
                return@apply
            }
            config.handlers.reversed().forEach { handler ->
                if (handler.isHandler(any)) {
                    addAll(handler.handler(config, any, columns))
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