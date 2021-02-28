package chooongg.box.core.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.LinearLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import chooongg.box.core.R
import chooongg.box.core.interfaces.BoxInit
import chooongg.box.core.widget.BoxToolBar
import chooongg.box.ext.attrDimensionPixelSize
import chooongg.box.ext.contentView
import chooongg.box.ext.dp2px

abstract class BoxActivity : AppCompatActivity, BoxInit {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    inline val context: Context get() = this

    inline val activity: Activity get() = this

    protected open fun isShowTopAppBar() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isShowTopAppBar()) initToolBar()
        onCreateToInitConfig(savedInstanceState)
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onPostCreateToInitContent(savedInstanceState)
    }

    protected open fun onCreateToInitConfig(savedInstanceState: Bundle?) {
        initConfig(savedInstanceState)
    }

    protected open fun onPostCreateToInitContent(savedInstanceState: Bundle?) {
        initContent(savedInstanceState)
    }

    protected open fun initToolBar() {
        val typedArray = theme.obtainStyledAttributes(intArrayOf(R.attr.toolbarStyle))
        val resourceId = typedArray.getResourceId(0, R.style.BoxWidget_Toolbar_PrimarySurface)
        val boxToolbar = BoxToolBar(context, null, 0, resourceId)
        boxToolbar.minimumHeight = attrDimensionPixelSize(R.attr.actionBarSize, dp2px(56f))
        val parentLayout = contentView.parent as LinearLayout
        parentLayout.addView(
            boxToolbar, 0,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        setSupportActionBar(boxToolbar)
        typedArray.recycle()
    }
}