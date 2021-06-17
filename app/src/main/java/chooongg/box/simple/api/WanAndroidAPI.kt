package chooongg.box.simple.api

import chooongg.box.http.RetrofitManager
import chooongg.box.simple.modules.main.MainActivity
import retrofit2.http.GET
import retrofit2.http.Query

interface WanAndroidAPI {

    companion object {
        fun get() =
            RetrofitManager.getAPI(WanAndroidAPI::class.java, "https://wanandroid.com/maven_pom/")
    }

    /**
     * 获取所有包名
     */
    @GET("package/json")
    suspend fun allPackage(): MainActivity.WanAndroidAPIResponse<ArrayList<String>>

    /**
     * 根据Key搜索包名信息
     */
    @GET("search/json")
    suspend fun searchPackage(@Query("k") key: String): String

}