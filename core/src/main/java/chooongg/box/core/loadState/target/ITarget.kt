package chooongg.box.core.loadState.target

import chooongg.box.core.loadState.LoadLayout
import chooongg.box.core.loadState.callback.Callback
import kotlin.reflect.KClass

interface ITarget {

    fun withEquals(target: Any?): Boolean

    fun replaceView(target: Any, onReloadListener: ((KClass<out Callback>) -> Unit)?): LoadLayout

}