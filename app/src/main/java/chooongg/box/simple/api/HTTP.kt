package chooongg.box.simple.api

import chooongg.box.http.RetrofitManager

object HTTP {

    /**
     * 玩Android Google Maven 仓库快速查询
     */
    fun apiWanAndroid() = RetrofitManager.getAPI(WanAndroidAPI::class)

}