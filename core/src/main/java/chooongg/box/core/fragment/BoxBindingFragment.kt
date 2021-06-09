package chooongg.box.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import java.lang.reflect.ParameterizedType

abstract class BoxBindingFragment<VB : ViewBinding> : BoxFragment() {

    protected lateinit var binding: VB

    override fun boxCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return createViewForViewBinding()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun createViewForViewBinding(): View? {
        val type = javaClass.genericSuperclass
        return if (type is ParameterizedType) {
            val clazz = type.actualTypeArguments[0] as Class<*>
            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
            binding = method.invoke(null, layoutInflater) as VB
            binding.root
        } else null
    }

}