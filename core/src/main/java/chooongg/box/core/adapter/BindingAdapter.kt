package chooongg.box.core.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import chooongg.box.log.BoxLog
import chooongg.box.log.LogEntity
import com.chad.library.adapter.base.BaseQuickAdapter
import java.lang.reflect.ParameterizedType

abstract class BindingAdapter<T, BINDING : ViewBinding> :
    BaseQuickAdapter<T, BindingHolder<BINDING>>(0) {

    @Suppress("UNCHECKED_CAST")
    override fun onCreateDefViewHolder(parent: ViewGroup, viewType: Int): BindingHolder<BINDING> {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[1] as Class<*>
                val method = clazz.getMethod(
                    "inflate",
                    LayoutInflater::class.java,
                    ViewGroup::class.java,
                    Boolean::class.java
                )
                val binding =
                    method.invoke(null, LayoutInflater.from(context), parent, false) as BINDING
                return BindingHolder(binding)
            } catch (e: Exception) {
                BoxLog.e(
                    LogEntity(javaClass.simpleName, "ViewBinding initialization failed"),
                    false
                )
            }
        }
        return super.onCreateDefViewHolder(parent, viewType)
    }
}