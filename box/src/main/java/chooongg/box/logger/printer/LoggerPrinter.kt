package chooongg.box.logger.printer

import chooongg.box.logger.Logger

interface LoggerPrinter {

    /**
     * 打印日志
     */
    fun printLog(@Logger.LogLevel logLevel: Int, tag: String, msg: String)
}