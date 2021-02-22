package chooongg.box.log.handler

import chooongg.box.log.formatter.Formatter

/**
 * Log处理
 */
interface LogHandler {

    /**
     * 子标签
     */
    fun getChildTag(any: Any): String?

    /**
     * 是否能处理
     */
    fun isHandler(any: Any): Boolean

    /**
     * 处理
     */
    fun handler(formatter: Formatter, any: Any): List<String>

}