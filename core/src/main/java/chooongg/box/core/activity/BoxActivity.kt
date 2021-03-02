package chooongg.box.core.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.FitWindowsLinearLayout
import androidx.appcompat.widget.Toolbar
import chooongg.box.core.R
import chooongg.box.core.interfaces.BoxInit
import chooongg.box.core.widget.BoxToolBar
import chooongg.box.ext.contentView
import chooongg.box.log.BoxLog

abstract class BoxActivity : AppCompatActivity(), BoxInit {

    inline val context: Context get() = this

    inline val activity: Activity get() = this

    open fun isShowToolBar() = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (isShowToolBar()) {
            val parentLayout = contentView.parent as FitWindowsLinearLayout
            val boxToolBar = initToolBar(parentLayout)
            parentLayout.addView(boxToolBar, 0)
            setSupportActionBar(boxToolBar)
        }
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

    protected open fun initToolBar(parentLayout: FitWindowsLinearLayout): Toolbar {
        return layoutInflater.inflate(
            R.layout.box_activity_toolbar,
            parentLayout,
            false
        ) as BoxToolBar
    }

    override fun onNightModeChanged(mode: Int) {
        super.onNightModeChanged(mode)
        BoxLog.e(mode)
    }
}