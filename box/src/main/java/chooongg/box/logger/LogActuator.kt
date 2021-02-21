package chooongg.box.logger

object LogActuator {

    fun log(config: LogConfig, @LogConfig.Level level: Int, tag: String, vararg any: Any?) {
        if (!config.enable) return

    }

}