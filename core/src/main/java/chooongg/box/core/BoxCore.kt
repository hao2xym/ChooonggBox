package chooongg.box.core

import android.app.Application
import android.os.Build
import android.util.Log
import androidx.annotation.Keep
import androidx.transition.Explode
import chooongg.box.Box
import chooongg.box.core.permission.PermissionInterceptor
import chooongg.box.ext.isAppDebug
import coil.Coil
import coil.ImageLoader
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.hjq.permissions.XXPermissions

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
        Coil.setImageLoader(ImageLoader.Builder(application)
            .componentRegistry {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder(application))
                } else {
                    add(GifDecoder())
                }
            }.build())
    }
}