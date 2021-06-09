package chooongg.box.core.activity

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import chooongg.box.log.BoxLog
import chooongg.box.log.LogEntity
import java.lang.reflect.ParameterizedType

/**
 * BoxActivity of Bind ViewModel
 */
abstract class BoxModelActivity<VM : ViewModel> : BoxActivity {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected lateinit var model: VM

    override fun onCreateToInitConfig(savedInstanceState: Bundle?) {
        getViewModelForViewModel()
        super.onCreateToInitConfig(savedInstanceState)
    }

    @Suppress("UNCHECKED_CAST")
    protected fun getViewModelForViewModel() {
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            try {
                val clazz = type.actualTypeArguments[0] as Class<VM>
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
