package chooongg.box.core.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import chooongg.box.log.BoxLog
import chooongg.box.log.LogEntity
import java.lang.reflect.ParameterizedType

abstract class BoxBindingModelFragment<VB : ViewBinding, VM : ViewModel> :
    BoxBindingFragment<VB>() {

    protected lateinit var model: VM

    override fun onCreateViewToInitConfig(savedInstanceState: Bundle?) {
        getViewModelForViewModel()
        super.onCreateViewToInitConfig(savedInstanceState)
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

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}