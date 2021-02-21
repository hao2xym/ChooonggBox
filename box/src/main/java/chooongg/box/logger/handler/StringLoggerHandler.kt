package chooongg.box.logger.handler

object StringLoggerHandler:LoggerHandler<Any> {
    override fun isHandler(any: Any) = true

    override fun handler(any: Any): Boolean {
        return true
    }
}