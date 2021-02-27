package chooongg.box.ext

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt

fun Context.attrText(@AttrRes id: Int): CharSequence {
    val a = obtainStyledAttributes(intArrayOf(id))
    val text = a.getText(0)
    a.recycle()
    return text
}

fun Context.attrString(@AttrRes id: Int): String? {
    val a = obtainStyledAttributes(intArrayOf(id))
    val string = a.getString(0)
    a.recycle()
    return string
}

fun Context.attrBoolean(@AttrRes id: Int, defValue: Boolean): Boolean {
    val a = obtainStyledAttributes(intArrayOf(id))
    val boolean = a.getBoolean(0, defValue)
    a.recycle()
    return boolean
}

fun Context.attrInt(@AttrRes id: Int, defValue: Int): Int {
    val a = obtainStyledAttributes(intArrayOf(id))
    val int = a.getInt(0, defValue)
    a.recycle()
    return int
}

fun Context.attrFloat(@AttrRes id: Int, defValue: Float): Float {
    val a = obtainStyledAttributes(intArrayOf(id))
    val float = a.getFloat(0, defValue)
    a.recycle()
    return float
}

fun Context.attrColor(@AttrRes id: Int, @ColorInt defValue: Int): Int {
    val a = obtainStyledAttributes(intArrayOf(id))
    val color = a.getColor(0, defValue)
    a.recycle()
    return color
}

fun Context.attrColorStateList(@AttrRes id: Int): ColorStateList? {
    val a = obtainStyledAttributes(intArrayOf(id))
    val colorStateList = a.getColorStateList(0)
    a.recycle()
    return colorStateList
}

fun Context.attrInteger(@AttrRes id: Int, defValue: Int): Int {
    val a = obtainStyledAttributes(intArrayOf(id))
    val integer = a.getInteger(0, defValue)
    a.recycle()
    return integer
}

fun Context.attrDimension(@AttrRes id: Int, defValue: Float): Float {
    val a = obtainStyledAttributes(intArrayOf(id))
    val dimension = a.getDimension(0, defValue)
    a.recycle()
    return dimension
}

fun Context.attrDimensionPixelOffset(@AttrRes id: Int, defValue: Int): Int {
    val a = obtainStyledAttributes(intArrayOf(id))
    val dimension = a.getDimensionPixelOffset(0, defValue)
    a.recycle()
    return dimension
}


fun Context.attrDimensionPixelSize(@AttrRes id: Int, defValue: Int): Int {
    val a = obtainStyledAttributes(intArrayOf(id))
    val dimension = a.getDimensionPixelSize(0, defValue)
    a.recycle()
    return dimension
}

fun Context.attrResourcesId(@AttrRes id: Int, defValue: Int): Int {
    val a = obtainStyledAttributes(intArrayOf(id))
    val resourcesId = a.getResourceId(0, defValue)
    a.recycle()
    return resourcesId
}

fun Context.attrDrawable(@AttrRes id: Int): Drawable? {
    val a = obtainStyledAttributes(intArrayOf(id))
    val drawable = a.getDrawable(0)
    a.recycle()
    return drawable
}