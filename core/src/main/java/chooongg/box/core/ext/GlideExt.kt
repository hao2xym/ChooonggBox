package chooongg.box.core.ext

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.annotation.DrawableRes
import chooongg.box.core.R
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import java.io.File
import java.net.URL

fun ImageView.loadAny(
    any: Any?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) {
    val requestBuilder = Glide.with(this).load(any)
        .useAnimationPool(true)
        .fallback(R.color.color_divider)
        .placeholder(R.color.color_divider)
        .error(R.color.color_divider)
    builder(requestBuilder)
    requestBuilder.into(this)
}

fun ImageView.load(
    uri: String?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(uri, builder)

fun ImageView.load(
    url: URL?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(url, builder)

fun ImageView.load(
    uri: Uri?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(uri, builder)

fun ImageView.load(
    file: File?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(file, builder)

fun ImageView.load(
    @DrawableRes drawableResId: Int,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(drawableResId, builder)

fun ImageView.load(
    drawable: Drawable?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(drawable, builder)

fun ImageView.load(
    bitmap: Bitmap?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(bitmap, builder)