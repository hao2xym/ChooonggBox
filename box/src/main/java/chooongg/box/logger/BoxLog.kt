package chooongg.box.logger

import android.util.Log

object BoxLog {

    private var config = LogConfig()

    fun config(block: LogConfig.() -> Unit) {
        val config = LogConfig()
        block.invoke(config)
        this.config = config
    }

    fun v(vararg anys: Any?) = vTag(config.tag, anys)
    fun d(vararg anys: Any?) = dTag(config.tag, anys)
    fun i(vararg anys: Any?) = iTag(config.tag, anys)
    fun w(vararg anys: Any?) = wTag(config.tag, anys)
    fun e(vararg anys: Any?) = eTag(config.tag, anys)
    fun a(vararg anys: Any?) = aTag(config.tag, anys)
    fun vTag(tag: String, vararg anys: Any?) = LogActuator.log(config, Log.VERBOSE, tag, anys)
    fun dTag(tag: String, vararg anys: Any?) = LogActuator.log(config, Log.DEBUG, tag, anys)
    fun iTag(tag: String, vararg anys: Any?) = LogActuator.log(config, Log.INFO, tag, anys)
    fun wTag(tag: String, vararg anys: Any?) = LogActuator.log(config, Log.WARN, tag, anys)
    fun eTag(tag: String, vararg anys: Any?) = LogActuator.log(config, Log.ERROR, tag, anys)
    fun aTag(tag: String, vararg anys: Any?) = LogActuator.log(config, Log.ASSERT, tag, anys)
}