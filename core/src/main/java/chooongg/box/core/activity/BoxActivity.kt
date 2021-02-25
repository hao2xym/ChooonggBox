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

    @Deprecated(
        "请使用初始化方法进行初始化",
        ReplaceWith("initConfig(savedInstanceState)"),
        DeprecationLevel.ERROR
    )
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        // 初始化配置
        initConfig(savedInstanceState)
        // 初始化内容
        initContent(savedInstanceState)
    }
}