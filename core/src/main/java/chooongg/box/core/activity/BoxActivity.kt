package chooongg.box.core.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.FitWindowsLinearLayout
import androidx.appcompat.widget.Toolbar
import chooongg.box.core.R
import chooongg.box.core.interfaces.BoxInit
import chooongg.box.core.manager.HideKeyboardManager
import chooongg.box.core.widget.BoxToolBar
import chooongg.box.ext.contentView
import chooongg.box.ext.loadActivityLabel
import chooongg.box.ext.resourcesDimension
import chooongg.box.log.BoxLog
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlin.reflect.full.findAnnotation

abstract class BoxActivity(@LayoutRes private val contentLayoutId: Int? = null) :
    AppCompatActivity(), BoxInit {

    inline val context: Context get() = this

    inline val activity: Activity get() = this

    protected open fun isAutoHideKeyBoard() = true

    protected open fun initTransition() = Unit

    protected var toolbar: Toolbar? = null

    open fun isShowActionBar() = true

    protected open fun configToolBar(parentLayout: FitWindowsLinearLayout) = layoutInflater.inflate(
        R.layout.box_activity_toolbar,
        parentLayout,
        false
    ) as BoxToolBar

    protected open fun initToolBar(toolbar: Toolbar) = Unit

    protected open fun isAutoShowNavigationIcon() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        configThemeForAnnotation()
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        super.onCreate(savedInstanceState)
        if (isShowActionBar()) {
            val parentLayout = contentView.parent as FitWindowsLinearLayout
            toolbar = configToolBar(parentLayout).apply {
                id = R.id.box_toolbar
                elevation = resourcesDimension(R.dimen.design_appbar_elevation)
            }
            parentLayout.addView(toolbar, 0)
            setSupportActionBar(toolbar)
            if (isAutoShowNavigationIcon()) {
                toolbar!!.setNavigationIcon(R.drawable.ic_app_bar_back)
                toolbar!!.setNavigationOnClickListener {
                    BoxLog.e("OnBackPressed")
                    onBackPressed()
                }
            }
            supportActionBar?.title = loadActivityLabel()
        }
        initTransition()
        if (toolbar != null) initToolBar(toolbar!!)
        if (contentLayoutId != null) {
            setContentView(contentLayoutId)
        }
        onCreateToInitConfig(savedInstanceState)
        if (isAutoHideKeyBoard()) HideKeyboardManager.init(activity)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onPostCreateToInitContent(savedInstanceState)
    }

    private fun configThemeForAnnotation() {
        val theme = this::class.findAnnotation<Theme>()
        if (theme != null) setTheme(theme.value)
    }

    protected open fun onCreateToInitConfig(savedInstanceState: Bundle?) {
        initConfig(savedInstanceState)
    }

    protected open fun onPostCreateToInitContent(savedInstanceState: Bundle?) {
        initContent(savedInstanceState)
    }
}