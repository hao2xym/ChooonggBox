package chooongg.box.log

import android.util.Log

object BoxLog {

    private var config = LogConfig()

    fun config(block: LogConfig.() -> Unit) {
        val config = LogConfig()
        block.invoke(config)
        this.config = config
    }

    fun v(vararg any: Any?) = vTagDouble(null, null, *any)
    fun d(vararg any: Any?) = dTagDouble(null, null, *any)
    fun i(vararg any: Any?) = iTagDouble(null, null, *any)
    fun w(vararg any: Any?) = wTagDouble(null, null, *any)
    fun e(vararg any: Any?) = eTagDouble(null, null, *any)
    fun a(vararg any: Any?) = aTagDouble(null, null, *any)
    fun log(vararg any: Any?) = logTagDouble(null, null, *any)

    fun vTag(tag: String, vararg any: Any?) = vTagDouble(tag, null, *any)
    fun dTag(tag: String, vararg any: Any?) = dTagDouble(tag, null, *any)
    fun iTag(tag: String, vararg any: Any?) = iTagDouble(tag, null, *any)
    fun wTag(tag: String, vararg any: Any?) = wTagDouble(tag, null, *any)
    fun eTag(tag: String, vararg any: Any?) = eTagDouble(tag, null, *any)
    fun aTag(tag: String, vararg any: Any?) = aTagDouble(tag, null, *any)
    fun logTag(tag: String, vararg any: Any?) = logTagDouble(tag, null, *any)

    fun vTagChild(childTag: String, vararg any: Any?) = vTagDouble(null, childTag, *any)
    fun dTagChild(childTag: String, vararg any: Any?) = dTagDouble(null, childTag, *any)
    fun iTagChild(childTag: String, vararg any: Any?) = iTagDouble(null, childTag, *any)
    fun wTagChild(childTag: String, vararg any: Any?) = wTagDouble(null, childTag, *any)
    fun eTagChild(childTag: String, vararg any: Any?) = eTagDouble(null, childTag, *any)
    fun aTagChild(childTag: String, vararg any: Any?) = aTagDouble(null, childTag, *any)
    fun logTagChild(childTag: String, vararg any: Any?) = logTagDouble(null, childTag, *any)

    fun vTagDouble(tag: String?, childTag: String?, vararg any: Any?) =
        LogActuator.log(config, Log.VERBOSE, tag, childTag, *any)

    fun dTagDouble(tag: String?, childTag: String?, vararg any: Any?) =
        LogActuator.log(config, Log.DEBUG, tag, childTag, *any)

    fun iTagDouble(tag: String?, childTag: String?, vararg any: Any?) =
        LogActuator.log(config, Log.INFO, tag, childTag, *any)

    fun wTagDouble(tag: String?, childTag: String?, vararg any: Any?) =
        LogActuator.log(config, Log.WARN, tag, childTag, *any)

    fun eTagDouble(tag: String?, childTag: String?, vararg any: Any?) =
        LogActuator.log(config, Log.ERROR, tag, childTag, *any)

    fun aTagDouble(tag: String?, childTag: String?, vararg any: Any?) =
        LogActuator.log(config, Log.ASSERT, tag, childTag, *any)

    fun logTagDouble(tag: String?, childTag: String?, vararg any: Any?) =
        LogActuator.log(config, null, tag, childTag, *any)
}