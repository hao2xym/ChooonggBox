package chooongg.box.core.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import chooongg.box.core.interfaces.BoxInit

abstract class BoxActivity : AppCompatActivity, BoxInit {

    constructor() : super()
    constructor(@LayoutRes contentLayoutId: Int) : super(contentLayoutId)

    inline val context: Context get() = this

    inline val activity: Activity get() = this

    open fun initAppBar() {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

    protected open fun initToolBar(){


    }
}