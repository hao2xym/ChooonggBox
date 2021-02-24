package chooongg.box.log.handler

import chooongg.box.log.LogActuator
import chooongg.box.log.LogConfig
import chooongg.box.log.LogConstant

object CollectionLogHandler : LogHandler {

    override fun isHandler(any: Any) = any is Collection<*>

    override fun handler(config: LogConfig, any: Any) = ArrayList<String>().apply {
        val collection = any as Collection<*>
        val step = LogConstant.stepBlank()
        if (collection.isEmpty()) {
            add("[]")
            return@apply
        }
        add("[")
        collection.forEach { item ->
            LogActuator.handlerLoop(config, item).forEach {
                add("${step}${it}")
            }
        }
        add("]")
    }

}