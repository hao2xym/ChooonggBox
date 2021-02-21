package chooongg.box.logger.printer

import chooongg.box.logger.LogConfig

interface Printer {


    /**
     * 打印日志
     */
    fun printLog(@LogConfig.Level logLevel: Int, tag: String, log: String)
}