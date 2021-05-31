package chooongg.box.core.loadState

import chooongg.box.core.loadState.callback.Callback
import chooongg.box.core.loadState.target.ActivityTarget
import chooongg.box.core.loadState.target.ITarget
import chooongg.box.core.loadState.target.ViewTarget
import kotlin.reflect.KClass

class LoadUtils {
    companion object {
        private val loadUtils: LoadUtils by lazy { LoadUtils() }

        fun getDefault() = loadUtils
    }

    private var builder: Builder

    private constructor() {
        builder = Builder()
    }

    private constructor(builder: Builder) {
        this.builder = builder
    }

    private fun setBuilder(builder: Builder) {
        this.builder = builder
    }

    fun register(
        target: Any,
        onReloadListener: ((Class<out Callback>) -> Unit)? = null
    ): LoadService<*> {
        return register<Any>(target, onReloadListener, null)
    }

    fun <T> register(
        target: Any,
        onReloadListener: ((Class<out Callback>) -> Unit)?,
        convertor: Convertor<T>?
    ): LoadService<T> {
        val targetContext = getTargetContext(target, builder.targetList)
        val loadLayout = targetContext.replaceView(target, onReloadListener)
        return LoadService(convertor, loadLayout, builder)
    }

    class Builder {
        val callbacks = ArrayList<KClass<out Callback>>()
        val targetList = ArrayList<ITarget>().apply {
            add(ActivityTarget())
            add(ViewTarget())
        }
        var defaultCallback: KClass<out Callback>? = null
            private set

        fun addCallback(callback: KClass<out Callback>) = apply {
            callbacks.add(callback)
        }

        fun addTargetContext(target: ITarget) = apply {
            targetList.add(target)
        }

        fun setDefaultCallback(defaultCallback: KClass<out Callback>) = apply {
            this.defaultCallback = defaultCallback
        }

        fun commit() = getDefault().setBuilder(this)

        fun build() = LoadUtils(this)
    }

    fun getTargetContext(target: Any?, targetContextList: List<ITarget>): ITarget {
        for (targetContext in targetContextList) {
            if (targetContext.withEquals(target)) {
                return targetContext
            }
        }
        throw IllegalArgumentException("No TargetContext fit it")
    }
}