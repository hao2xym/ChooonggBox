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
import chooongg.box.ext.contentView
import chooongg.box.log.BoxLog

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
        val parentLayout = contentView.parent as LinearLayout
        val boxToolbar =
            layoutInflater.inflate(R.layout.box_activity_toolbar, parentLayout, false) as BoxToolBar
        parentLayout.addView(
            boxToolbar, 0,
            LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
        )
        setSupportActionBar(boxToolbar)
    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
        BoxLog.e(mode)
    }
}