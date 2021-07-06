package chooongg.box.core.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import chooongg.box.core.R
import coil.ImageLoader
import coil.imageLoader
import coil.loadAny
import coil.request.Disposable
import coil.request.ImageRequest
import okhttp3.HttpUrl
import java.io.File

fun ImageView.loadAnyDefault(
    any: Any?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAny(any) {
    crossfade(true)
    fallback(R.drawable.layer_placeholder_image_error)
    placeholder(R.drawable.layer_placeholder_image_loading)
    error(R.drawable.layer_placeholder_image_error)
    builder(this)
}

fun ImageView.loadDefault(
    uri: String?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAnyDefault(uri, imageLoader, builder)

fun ImageView.loadDefault(
    url: HttpUrl?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAnyDefault(url, imageLoader, builder)

fun ImageView.loadDefault(
    uri: Uri?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAnyDefault(uri, imageLoader, builder)

fun ImageView.loadDefault(
    file: File?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAnyDefault(file, imageLoader, builder)

fun ImageView.loadDefault(
    @DrawableRes drawableResId: Int,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAnyDefault(drawableResId, imageLoader, builder)

fun ImageView.loadDefault(
    drawable: Drawable?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAnyDefault(drawable, imageLoader, builder)

fun ImageView.loadDefault(
    bitmap: Bitmap?,
    imageLoader: ImageLoader = context.imageLoader,
    builder: ImageRequest.Builder.() -> Unit = {}
): Disposable = loadAnyDefault(bitmap, imageLoader, builder)