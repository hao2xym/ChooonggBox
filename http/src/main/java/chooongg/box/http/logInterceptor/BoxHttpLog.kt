package chooongg.box.http.logInterceptor

import chooongg.box.log.LogActuator

object BoxHttpLog {

    internal var config = HttpLogConfig()

    fun config(block: HttpLogConfig.() -> Unit) {
        val config = HttpLogConfig()
        block.invoke(config)
        this.config = config
    }

    fun request(config: HttpLogConfig, vararg any: Any?) =
        LogActuator.log(config, config.requestLevel, config.tag, "↗↗↗↗↗↗↗↗ Request ↗↗↗↗↗↗↗↗", *any)

    fun response(config: HttpLogConfig, vararg any: Any?) =
        LogActuator.log(
            config,
            config.responseLevel,
            config.tag,
            "↘↘↘↘↘↘↘↘ Response ↘↘↘↘↘↘↘↘",
            *any
        )
}