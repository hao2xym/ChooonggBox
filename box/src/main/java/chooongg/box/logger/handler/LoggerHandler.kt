package chooongg.box.logger.handler

import androidx.annotation.IntRange

/**
 * Log处理
 */
interface LoggerHandler<T> {

    /**
     * 1为最大优先级
     */
    @IntRange(from = 1)
    fun priority(): Int

    /**
     * 是否能处理
     */
    fun isHandler(any: Any): Boolean

    /**
     * 处理
     */
    fun handler(any: Any): Boolean

}