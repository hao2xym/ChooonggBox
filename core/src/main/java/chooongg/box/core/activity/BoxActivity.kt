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
import chooongg.box.ext.loadLabel
import kotlin.reflect.full.findAnnotation

abstract class BoxActivity : AppCompatActivity, BoxInit {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    inline val context: Context get() = this

    inline val activity: Activity get() = this

    open fun isShowActionBar() = true

    protected open fun isAutoHideKeyBoard() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        configThemeForAnnotation()
        super.onCreate(savedInstanceState)
        if (isShowActionBar()) {
            val parentLayout = contentView.parent as FitWindowsLinearLayout
            val boxToolBar = initToolBar(parentLayout)
            parentLayout.addView(boxToolBar, 0)
            setSupportActionBar(boxToolBar)
            supportActionBar?.title = loadLabel()
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

    protected open fun initToolBar(parentLayout: FitWindowsLinearLayout): Toolbar {
        val boxToolBar = layoutInflater.inflate(
            R.layout.box_activity_toolbar,
            parentLayout,
            false
        ) as BoxToolBar
        return boxToolBar
    }
}