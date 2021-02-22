package chooongg.box.log.printer

import chooongg.box.log.LogConfig

interface Printer {


    /**
     * 打印日志
     */
    fun printLog(@LogConfig.Level logLevel: Int, tag: String, log: String)
}