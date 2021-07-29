package chooongg.box.core.fragment

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import chooongg.box.core.activity.BoxActivity
import chooongg.box.core.annotation.Title
import chooongg.box.core.interfaces.BoxInit

@Title("未命名")
abstract class BoxFragment : Fragment, BoxInit {

    internal constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    protected val boxActivity get() = if (activity is BoxActivity) activity as BoxActivity else null

    var isLoaded = false
        private set

    protected open fun onCreateViewToInitConfig(savedInstanceState: Bundle?) {
        initConfig(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onCreateViewToInitConfig(savedInstanceState)
        initContent(savedInstanceState)
    }

    abstract fun initContentByLazy()

    open fun onReselected() = Unit

    @CallSuper
    override fun onResume() {
        super.onResume()
        if (!isLoaded && !isHidden) {
            isLoaded = true
            initContentByLazy()
        }
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    fun showTipLoading(message: CharSequence? = null, isClickable: Boolean = false) {
        boxActivity?.showTipLoading(message, isClickable)
    }

    fun hideTipLoading() {
        boxActivity?.hideTipLoading()
    }

    private var title: CharSequence? = null

    fun setTitle(title: CharSequence?) = apply {
        this.title = title
    }

    fun getTitle() = when {
        title != null -> title
        javaClass.isAnnotationPresent(Title::class.java) -> {
            javaClass.getAnnotation(Title::class.java)!!.value
        }
        else -> "未命名"
    }
}