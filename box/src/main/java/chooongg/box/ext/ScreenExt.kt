package chooongg.box.ext

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources

fun Context.getScreenWidth() = resources.displayMetrics.widthPixels

fun Context.getScreenHeight() = resources.displayMetrics.heightPixels

fun getScreenDensity() = Resources.getSystem().displayMetrics.density

fun getScreenDensityDpi() = Resources.getSystem().displayMetrics.densityDpi

fun getScreenScaledDensity() = Resources.getSystem().displayMetrics.scaledDensity

fun Context.isLandscape() =
    resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun Context.isPortrait() =
    resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT