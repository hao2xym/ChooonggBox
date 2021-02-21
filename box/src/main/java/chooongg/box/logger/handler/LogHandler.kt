package chooongg.box.logger.handler

/**
 * Log处理
 */
interface LogHandler<T> {

    /**
     * 是否能处理
     */
    fun isHandler(any: Any?): Boolean

    /**
     * 处理
     */
    fun handler(any: Any): List<String>

}