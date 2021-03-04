package chooongg.box.log.handler

import chooongg.box.log.LogConfig

/**
 * Log处理
 */
interface LogHandler {

    /**
     * 是否能处理
     */
    fun isHandler(any: Any): Boolean

    fun getTypeString(any: Any): String?

    /**
     * 处理
     */
    fun handler(config: LogConfig, any: Any, columns: Int): List<String>

}