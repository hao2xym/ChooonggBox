package chooongg.box.logger.printer

import android.util.Log
import chooongg.box.ext.getByteUTF8Length
import chooongg.box.logger.LogConfig
import chooongg.box.logger.LogConstant

object LogcatPrinter : Printer {

    private const val MAX_LENGTH = 4000

    override fun printLog(@LogConfig.Level logLevel: Int, tag: String, msg: String) {
        if (msg.getByteUTF8Length() > MAX_LENGTH) {
            val sp = msg.split(LogConstant.BR)
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
                while (i < msg.length) {
                    if (i + MAX_LENGTH < msg.length) {
                        if (i == 0) printLog(logLevel, tag, msg.substring(i, i + MAX_LENGTH))
                        else printLog(logLevel, "", msg.substring(i, i + MAX_LENGTH))
                    } else printLog(logLevel, "", msg.substring(i, msg.length))
                    i += MAX_LENGTH
                }
            }
        } else {
            Log.println(logLevel, tag, msg)
        }
    }

    override fun equals(other: Any?) = other == this
    override fun hashCode() = javaClass.hashCode()
}