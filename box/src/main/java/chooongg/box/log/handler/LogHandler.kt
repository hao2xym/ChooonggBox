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

    /**
     * 处理
     */
    fun handler(config: LogConfig, any: Any): List<String>

}