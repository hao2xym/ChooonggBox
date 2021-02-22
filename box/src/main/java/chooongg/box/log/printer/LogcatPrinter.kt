package chooongg.box.log.printer

import android.util.Log
import chooongg.box.ext.getByteUTF8Length
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant

object LogcatPrinter : Printer {

    private const val MAX_LENGTH = 4000

    override fun printLog(@LogConfig.Level logLevel: Int, tag: String, log: String) {
        if (log.getByteUTF8Length() > MAX_LENGTH) {
            val sp = log.split(LogConstant.BR)
            if (sp.size > 1) {
                sp.forEachIndexed { index, s ->
                    if (index == 0) {
                        printLog(logLevel, tag, s)
                    } else {
                        printLog(logLevel, "", s)
                    }
                }
            } else {
                var i = 0
                while (i < log.length) {
                    if (i + MAX_LENGTH < log.length) {
                        if (i == 0) printLog(logLevel, tag, log.substring(i, i + MAX_LENGTH))
                        else printLog(logLevel, "", log.substring(i, i + MAX_LENGTH))
                    } else printLog(logLevel, "", log.substring(i, log.length))
                    i += MAX_LENGTH
                }
            }
        } else {
            Log.println(logLevel, tag, log)
        }
    }

    override fun equals(other: Any?) = other == this
    override fun hashCode() = javaClass.hashCode()
}