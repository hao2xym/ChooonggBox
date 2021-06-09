package chooongg.box.core.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import chooongg.box.log.BoxLog
import chooongg.box.log.LogEntity
import java.lang.reflect.ParameterizedType

/**
 * BoxActivity of Bind ViewBinding
 */
abstract class BoxBindingActivity<VB : ViewBinding> : BoxActivity() {

    protected lateinit var binding: VB

    override fun onCreateToInitConfig(savedInstanceState: Bundle?) {
        setContentViewForViewBinding()
        initConfig(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun setContentViewForViewBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[0] as Class<*>
                val method = clazz.getMethod("inflate", LayoutInflater::class.java)
                binding = method.invoke(null, layoutInflater) as VB
                setContentView(binding.root)
            } catch (e: Exception) {
                BoxLog.e(
                    LogEntity(javaClass.simpleName, "ViewBinding initialization failed"),
                    false
                )
            }
        }
    }

}