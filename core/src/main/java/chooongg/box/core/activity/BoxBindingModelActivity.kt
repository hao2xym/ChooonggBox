package chooongg.box.core.activity

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import chooongg.box.log.BoxLog
import chooongg.box.log.LogEntity
import java.lang.reflect.ParameterizedType

/**
 * BoxActivity of Bind ViewBinding
 */
abstract class BoxBindingModelActivity<VB : ViewBinding, VM : ViewModel> : BoxBindingActivity<VB>() {

    protected lateinit var model: VM

    override fun onCreateToInitConfig(savedInstanceState: Bundle?) {
        setContentViewForViewBinding()
        getViewModelForViewModel()
        initConfig(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewModelForViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[1] as Class<VM>
                model = ViewModelProvider(this).get(clazz)
            } catch (e: Exception) {
                BoxLog.e(
                    LogEntity(javaClass.simpleName, "ViewModel initialization failed"),
                    false
                )
            }
        }
    }
}