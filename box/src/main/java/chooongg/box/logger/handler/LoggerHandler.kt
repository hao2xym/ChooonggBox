package chooongg.box.logger.handler

/**
 * Log处理
 */
interface LoggerHandler<T> {

    /**
     * 是否能处理
     */
    fun isHandler(any: Any): Boolean

    /**
     * 处理
     */
    fun handler(any: Any): Boolean

}