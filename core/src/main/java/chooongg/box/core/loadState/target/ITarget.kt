package chooongg.box.core.loadState.target

import chooongg.box.core.loadState.LoadLayout
import chooongg.box.core.loadState.callback.Callback

interface ITarget {

    fun withEquals(target: Any?): Boolean

    fun replaceView(target: Any, onReloadListener: ((Class<out Callback>) -> Unit)?): LoadLayout

}