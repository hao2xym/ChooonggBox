package chooongg.box.core.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import chooongg.box.core.activity.BoxActivity
import chooongg.box.core.annotation.Title
import chooongg.box.core.interfaces.BoxInit
import kotlin.reflect.full.findAnnotation

@Title("未命名")
abstract class BoxFragment : Fragment, BoxInit {

    internal constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected val boxActivity get() = if (activity is BoxActivity) activity as BoxActivity else null

    private var isLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = boxCreateView(inflater, container, savedInstanceState)
        onCreateViewToInitConfig(savedInstanceState)
        return view
    }

    open fun boxCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    protected open fun onCreateViewToInitConfig(savedInstanceState: Bundle?) {
        initConfig(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initContent(savedInstanceState)
    }

    abstract fun initContentByLazy()

    open fun onReselected() = Unit

    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            isLoaded = true
            initContentByLazy()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    fun getTitle() = this::class.findAnnotation<Title>()?.value ?: "未命名"
}