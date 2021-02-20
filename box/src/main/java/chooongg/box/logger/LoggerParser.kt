package chooongg.box.logger

import chooongg.box.logger.formatter.Formatter

/**
 * 将对象解析成特定的字符串
 */
interface LoggerParser<T> {

    /**
     * 将对象解析成字符串
     */
    fun parseString(t: T,formatter: Formatter): String
}