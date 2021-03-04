package chooongg.box.core.activity

import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import chooongg.box.core.manager.HideKeyboardManager
import chooongg.box.log.BoxLog
import chooongg.box.log.LogEntity
import java.lang.reflect.ParameterizedType

abstract class BoxViewBindingActivity<T : ViewBinding> : BoxActivity() {

    protected lateinit var binding: T

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewForViewBinding()
        initConfig(savedInstanceState)
        if (isAutoHideKeyBoard()){
            HideKeyboardManager.init(activity)
        }
    }

    @Suppress("UNCHECKED_CAST")
    protected open fun setContentViewForViewBinding() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[0] as Class<*>
                val method = clazz.getMethod("inflate", LayoutInflater::class.java)
                binding = method.invoke(null, layoutInflater) as T
                setContentView(binding.root)
            } catch (e: Exception) {
                BoxLog.e(LogEntity(javaClass.simpleName, "ViewBinding initialization failed"), false)
            }
        }
    }

    override fun onCreateToInitConfig(savedInstanceState: Bundle?) = Unit
}