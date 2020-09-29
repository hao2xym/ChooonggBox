package chooongg.box.ext

import android.view.View

fun View.visible() = apply { if (visibility != View.VISIBLE) visibility = View.VISIBLE }
fun View.inVisible() = apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }
fun View.gone() = apply { if (visibility != View.GONE) visibility = View.GONE }

var lastClickTime = 0L

fun clickValid(): Boolean {
    val currentTime = System.currentTimeMillis()
    return if (currentTime - lastClickTime > CLICK_INTERVAL) {
        lastClickTime = currentTime
        true
    } else false
}

fun View.doOnClick(listener: ((View) -> Unit)?) = setOnClickListener {
    if (clickValid()) listener?.invoke(this)
}

fun View.doOnLongClick(listener: ((View) -> Boolean)?) = setOnLongClickListener {
    if (clickValid()) return@setOnLongClickListener listener?.invoke(this) ?: false
    return@setOnLongClickListener false
}

fun doOnClick(vararg views: View, listener: ((View) -> Unit)?) {
    views.forEach {
        it.setOnClickListener { view ->
            if (clickValid()) listener?.invoke(view)
        }
    }
}

fun doOnLongClick(vararg views: View, listener: ((View) -> Boolean)?) {
    views.forEach {
        it.setOnLongClickListener { view ->
            if (clickValid()) return@setOnLongClickListener listener?.invoke(view) ?: false
            return@setOnLongClickListener false
        }
    }
}