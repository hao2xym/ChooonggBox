//package chooongg.box.core.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.viewbinding.ViewBinding
//import com.chad.library.adapter.base.BaseQuickAdapter
//import java.lang.reflect.ParameterizedType
//
//abstract class BoxAdapter<T, VB : ViewBinding>(data: MutableList<T>? = null) :
//    BaseQuickAdapter<T, VBHolder<VB>>(0, data) {
//
//    @Suppress("UNCHECKED_CAST")
//    override fun onCreateDefViewHolder(
//        parent: ViewGroup,
//        viewType: Int
//    ): VBHolder<VB> {
//        val type = javaClass.genericSuperclass
//        if (type is ParameterizedType) {
//            val clazz = type.actualTypeArguments[1] as Class<*>
//            val method = clazz.getMethod("inflate", LayoutInflater::class.java)
//            return VBHolder(method.invoke(null, LayoutInflater.from(context)) as VB)
//        } else throw ClassCastException("Type is not ParameterizedType!")
//    }
//}