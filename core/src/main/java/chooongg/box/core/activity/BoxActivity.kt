package chooongg.box.core.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.Window
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ContentFrameLayout
import androidx.appcompat.widget.FitWindowsLinearLayout
import androidx.appcompat.widget.Toolbar
import chooongg.box.core.R
import chooongg.box.core.ext.setDefaultNavigation
import chooongg.box.core.interfaces.BoxInit
import chooongg.box.core.manager.HideKeyboardManager
import chooongg.box.core.widget.BoxToolBar
import chooongg.box.ext.loadActivityLabel
import chooongg.box.log.BoxLog
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

abstract class BoxActivity(@LayoutRes private val contentLayoutId: Int? = null) :
    AppCompatActivity(), BoxInit {

    val Activity.contentView: ContentFrameLayout by lazy { findViewById(Window.ID_ANDROID_CONTENT) }

    inline val context: Context get() = this

    inline val activity: Activity get() = this

    protected open fun isAutoHideKeyBoard() = true

    protected open fun initTransition() = Unit

    protected var toolbar: Toolbar? = null

    open fun isShowActionBar() = true

    protected open fun getToolBar(parentLayout: FitWindowsLinearLayout) = layoutInflater.inflate(
        R.layout.box_activity_toolbar,
        parentLayout,
        false
    ) as BoxToolBar

    protected open fun initToolBar(toolbar: Toolbar) = Unit

    protected open fun isAutoShowNavigationIcon() = true

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        configThemeForAnnotation()
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        BoxLog.d("${this::class.simpleName}: onCreate")
        if (isShowActionBar()) configActionBar()
        initTransition()
        if (toolbar != null) initToolBar(toolbar!!)
        if (contentLayoutId != null) {
            setContentView(contentLayoutId)
        }
        onCreateToInitConfig(savedInstanceState)
        if (isAutoHideKeyBoard()) HideKeyboardManager.init(activity)
    }

    protected open fun configActionBar() {
        val parentLayout = contentView.parent as FitWindowsLinearLayout
        toolbar = getToolBar(parentLayout).apply {
            id = R.id.box_toolbar
            title = loadActivityLabel()
            if (isAutoShowNavigationIcon()) setDefaultNavigation()
        }
        parentLayout.addView(toolbar, 0)
        setSupportActionBar(toolbar)
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onPostCreateToInitContent(savedInstanceState)
    }

    private fun configThemeForAnnotation() {
        if (javaClass.isAnnotationPresent(Theme::class.java)) {
            setTheme(javaClass.getAnnotation(Theme::class.java)!!.value)
        }
    }

    protected open fun onCreateToInitConfig(savedInstanceState: Bundle?) {
        initConfig(savedInstanceState)
    }

    protected open fun onPostCreateToInitContent(savedInstanceState: Bundle?) {
        initContent(savedInstanceState)
    }

    @CallSuper
    override fun onBackPressed() {
        super.onBackPressed()
        BoxLog.d("${this::class.simpleName}: OnBackPressed")
    }
}