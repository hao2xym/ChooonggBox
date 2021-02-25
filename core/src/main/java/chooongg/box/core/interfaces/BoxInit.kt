package chooongg.box.core.interfaces

import android.os.Bundle

interface BoxInit {

    /**
     * 初始化配置
     */
    fun initConfig(savedInstanceState: Bundle?)

    /**
     * 初始化内容
     */
    fun initContent(savedInstanceState: Bundle?)

}