package chooongg.box.logger.handler

object AnyHandler : LogHandler<Any> {
    override fun isHandler(any: Any?) = true
    override fun handler(any: Any) = listOf(any.toString())
}