package chooongg.box.core.loadState

import chooongg.box.core.loadState.callback.Callback
import kotlin.reflect.KClass

interface Convertor<T> {
    fun map(t: T): KClass<out Callback>
}