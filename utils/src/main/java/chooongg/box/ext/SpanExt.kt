package chooongg.box.ext

import chooongg.box.utils.SpanUtils

fun CharSequence.style(init: SpanUtils.() -> Unit): SpanUtils = SpanUtils(this).apply {
    init()
    return this
}