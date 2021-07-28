package chooongg.box.core

import android.app.Application
import android.util.Log
import androidx.annotation.Keep
import chooongg.box.Box
import chooongg.box.core.permission.PermissionInterceptor
import chooongg.box.ext.isAppDebug
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.VideoFrameDecoder
import coil.fetch.VideoFrameFileFetcher
import coil.fetch.VideoFrameUriFetcher
import coil.util.CoilUtils
import com.facebook.stetho.Stetho
import com.hjq.permissions.XXPermissions
import okhttp3.OkHttpClient

@Keep
object BoxCore {

    @Keep
    @JvmStatic
    @Suppress("unused", "UNUSED_PARAMETER")
    fun initialize(application: Application) {
        Log.d(Box.TAG, "[initialize: ${BuildConfig.LIBRARY_PACKAGE_NAME}]")
        XXPermissions.setScopedStorage(true)
        XXPermissions.setDebugMode(isAppDebug())
        XXPermissions.setInterceptor(PermissionInterceptor())
        Stetho.initializeWithDefaults(application)
        Coil.setImageLoader(
            ImageLoader.Builder(application).crossfade(true)
                .error(R.color.color_divider)
                .fallback(R.color.color_divider)
                .placeholder(R.color.color_divider)
                .componentRegistry {
                    add(GifDecoder())
                    add(VideoFrameDecoder(application))
                    add(VideoFrameUriFetcher(application))
                    add(VideoFrameFileFetcher(application))
                }.okHttpClient(
                    OkHttpClient.Builder().cache(CoilUtils.createDefaultCache(application)).build()
                ).build()
        )
    }
}