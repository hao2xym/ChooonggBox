package chooongg.box.log.handler

import android.os.Bundle
import chooongg.box.log.LogConstant
import chooongg.box.log.formatter.Formatter

object BundleLogHandler : LogHandler {
    override fun getChildTag(any: Any) = any::class.simpleName
    override fun isHandler(any: Any) = any is Bundle
    override fun handler(formatter: Formatter, any: Any) = ArrayList<String>().apply {
        val bundle = any as Bundle
        val keys = bundle.keySet()
        if (keys.isEmpty()) {
            add("{}")
            return@apply
        }
        add("{")
        keys.forEach {
            val value = bundle.get(it)
            when {
                value == null -> {
                    add(LogConstant.stepBlank() + it + ": " + LogConstant.NONE)
                }
                value is Bundle -> {
                    handlerInner(formatter, value).forEachIndexed { index, s ->
                        if (index ==0){
                            add(LogConstant.stepBlank() + it + ": " + s)
                        }else{
                            add(LogConstant.stepBlank() + s)
                        }
                    }
                }
                else -> {
                    add(LogConstant.stepBlank() + it + ": " + value.toString())
                }
            }
        }
        add("}")
    }

    private fun handlerInner(formatter: Formatter, bundle: Bundle): List<String> =
        handler(formatter, bundle)
}