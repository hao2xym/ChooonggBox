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

fun ImageView.loadAny(
    any: Any?,
    block: RequestBuilder<Drawable>.() -> Unit = {}
) {
    val builder = Glide.with(context)
        .load(any)
        .placeholder(R.drawable.layer_placeholder_image_loading)
        .error(R.drawable.layer_placeholder_image_error)
    block(builder)
    builder.into(this)
}

fun ImageView.load(
    uri: Uri?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(uri, builder)

fun ImageView.load(
    url: File?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(url, builder)

fun ImageView.load(
    uri: Bitmap?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(uri, builder)

fun ImageView.load(
    file: String?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(file, builder)

fun ImageView.load(
    @DrawableRes drawableResId: Int,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(drawableResId, builder)

fun ImageView.load(
    bitmap: ByteArray?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(bitmap, builder)

fun ImageView.load(
    drawable: Drawable?,
    builder: RequestBuilder<Drawable>.() -> Unit = {}
) = loadAny(drawable, builder)
