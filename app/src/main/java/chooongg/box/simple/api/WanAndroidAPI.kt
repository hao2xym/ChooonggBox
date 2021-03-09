package chooongg.box.simple.api

import chooongg.box.http.BaseUrl
import retrofit2.http.GET
import retrofit2.http.Query

@BaseUrl("https://wanandroid.com/maven_pom/")
interface WanAndroidAPI {

    /**
     * 获取所有包名
     */
    @GET("package/json")
    suspend fun allPackage(): String

    /**
     * 根据Key搜索包名信息
     */
    @GET("search/json?k=viewpager2")
    suspend fun searchPackage(@Query("k") key: String): String

}