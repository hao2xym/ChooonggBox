//package chooongg.box.core.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.viewbinding.ViewBinding
//import com.mikepenz.fastadapter.binding.AbstractBindingItem
//import java.lang.reflect.ParameterizedType
//
//abstract class BindingItem<BINDING : ViewBinding> : AbstractBindingItem<BINDING>() {
//    @Suppress("UNCHECKED_CAST")
//    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): BINDING {
//        val type = javaClass.genericSuperclass
//        val clazz = (type as ParameterizedType).actualTypeArguments[0] as Class<*>
//        val method = clazz.getMethod(
//            "inflate",
//            LayoutInflater::class.java,
//            ViewGroup::class.java,
//            Boolean::class.java
//        )
//        return method.invoke(null, inflater, parent, false) as BINDING
//    }
//}