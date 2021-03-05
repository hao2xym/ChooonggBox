package chooongg.box.http.logInterceptor

import android.util.Log
import chooongg.box.http.BoxHttp
import chooongg.box.log.LogConfig

class HttpLogConfig : LogConfig() {

    init {
        tag = BoxHttp.javaClass.simpleName
    }

    /**
     * 日志显示方式
     */
    var httpLogLevel: HttpLogLevel = HttpLogLevel.BASIC

    /**
     * 请求日志等级
     */
    @Level
    var requestLevel: Int = Log.DEBUG

    /**
     * 响应日志等级
     */
    @Level
    var responseLevel: Int = Log.DEBUG
}