package chooongg.box.core.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import chooongg.box.log.BoxLog
import chooongg.box.log.LogBean
import java.lang.reflect.ParameterizedType

abstract class BoxViewBindingActivity<T : ViewBinding> : BoxActivity() {

    protected lateinit var binding: T

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[0] as Class<*>
                val method = clazz.getMethod("inflate", LayoutInflater::class.java)
                binding = method.invoke(null, layoutInflater) as T
            } catch (e: Exception) {
                BoxLog.e(LogBean(javaClass.simpleName, "ViewBinding initialization failed"), false)
            }
        }
        setContentView(binding.root)
    }
}