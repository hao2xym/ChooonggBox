package chooongg.box.log.printer

import chooongg.box.ext.getByteUTF8Length
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant

object LogcatPrinter : Printer {

    private const val MAX_LENGTH = 4000

    override fun printLog(@LogConfig.Level logLevel: Int, tag: String, log: String) {
        if (log.getByteUTF8Length() > MAX_LENGTH) {
            val sp = ArrayList(log.split(LogConstant.BR))
            if (sp.size > 1) {
                sp.forEach {
                    printLog(logLevel, tag, it)
                }
//                var index = 0
//                var temp = ""
//                while (sp.size > 1) {
//                    temp += sp[0]
//                    sp.removeAt(0)
//                    if (sp.size > 1 && temp.getByteUTF8Length() <= MAX_LENGTH - sp[0].getByteUTF8Length()) {
//                        temp += LogConstant.BR
//                    } else {
//                        if (index == 0) {
//                            index++
//                            printLog(logLevel, tag, temp)
//                        } else {
//                            printLog(logLevel, tag, temp)
//                        }
//                        temp = ""
//                    }
//                }
            } else {
                var i = 0
                while (i < log.length) {
                    if (i + MAX_LENGTH < log.length) {
                        if (i == 0) printLog(logLevel, tag, log.substring(i, i + MAX_LENGTH))
                        else printLog(logLevel, tag, log.substring(i, i + MAX_LENGTH))
                    } else printLog(logLevel, tag, log.substring(i, log.length))
                    i += MAX_LENGTH
                }
            }
        } else {
            val sp = ArrayList(log.split(LogConstant.BR))
            sp.forEach {
                printLog(logLevel, tag, it)
            }
        }
    }

    override fun equals(other: Any?) = other == this
    override fun hashCode() = javaClass.hashCode()
}