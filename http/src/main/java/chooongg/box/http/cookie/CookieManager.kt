package chooongg.box.http.cookie

import android.content.Context
import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl


class CookieManager(context: Context) : CookieJar {

    private val cookieStore: PersistentCookieStore = PersistentCookieStore(context)

    override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
        if (cookies.isNotEmpty()) {
            for (item in cookies) {
                cookieStore.add(url, item)
            }
        }
    }

    override fun loadForRequest(url: HttpUrl): List<Cookie> {
        return cookieStore[url]
    }
}