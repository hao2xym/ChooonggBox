package chooongg.box.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import chooongg.box.log.BoxLog
import chooongg.box.log.LogBean
import java.lang.reflect.ParameterizedType

abstract class BoxViewBindingActivity<T : ViewBinding> : BoxFragment() {

    protected lateinit var binding: T

    @Suppress("UNCHECKED_CAST")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[0] as Class<*>
                val method = clazz.getMethod("inflate", LayoutInflater::class.java)
                binding = method.invoke(null, layoutInflater) as T
                initConfig(savedInstanceState)
                return binding.root
            } catch (e: Exception) {
                BoxLog.e(LogBean(javaClass.simpleName, "ViewBinding initialization failed"), false)
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}