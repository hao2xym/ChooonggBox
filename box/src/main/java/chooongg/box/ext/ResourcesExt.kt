package chooongg.box.ext

import android.content.Context
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.*
import androidx.core.content.ContextCompat

fun Context.resourcesString(@StringRes id: Int) = resources.getString(id)
fun Context.resourcesString(@StringRes id: Int, vararg format: Any?) = resources.getString(id, *format)
fun Context.resourcesText(@StringRes id: Int) = resources.getText(id)
fun Context.resourcesTextArray(@ArrayRes id: Int): Array<CharSequence> = resources.getTextArray(id)
fun Context.resourcesStringArray(@ArrayRes id: Int): Array<String> = resources.getStringArray(id)
fun Context.resourcesIntArray(@ArrayRes id: Int) = resources.getIntArray(id)
fun Context.resourcesDimension(@DimenRes id: Int) = resources.getDimension(id)
fun Context.resourcesDimensionPixelOffset(@DimenRes id: Int) = resources.getDimensionPixelOffset(id)
fun Context.resourcesDimensionPixelSize(@DimenRes id: Int) = resources.getDimensionPixelSize(id)
fun Context.resourcesDrawable(@DrawableRes id: Int) = ContextCompat.getDrawable(this, id)
fun Context.resourcesColor(@ColorRes id: Int) = ContextCompat.getColor(this, id)
fun Context.resourcesColorStateList(@ColorRes id: Int) = ContextCompat.getColorStateList(this, id)
fun Context.resourcesBoolean(@BoolRes id: Int) = resources.getBoolean(id)
fun Context.resourcesInteger(@IntegerRes id: Int) = resources.getInteger(id)
fun Context.resourcesOpenRaw(@RawRes id: Int) = resources.openRawResource(id)
fun Context.resourcesAnimation(@AnimRes id: Int): Animation = AnimationUtils.loadAnimation(this, id)