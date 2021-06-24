package chooongg.box.ext

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewParent
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils

const val CLICK_INTERVAL = 600L

fun View.visible() = apply { if (visibility != View.VISIBLE) visibility = View.VISIBLE }
fun View.inVisible() = apply { if (visibility != View.INVISIBLE) visibility = View.INVISIBLE }
fun View.gone() = apply { if (visibility != View.GONE) visibility = View.GONE }

var lastClickTime = 0L

/**
 * 点击效验 防止快速双重点击
 */
fun clickValid(): Boolean {
    val currentTime = System.currentTimeMillis()
    return if (currentTime - lastClickTime > CLICK_INTERVAL) {
        lastClickTime = currentTime
        true
    } else false
}

/**
 * 点击事件
 */
fun View.doOnClick(listener: ((View) -> Unit)?) = setOnClickListener {
    if (clickValid()) listener?.invoke(this)
}

/**
 * 长按事件
 */
fun View.doOnLongClick(listener: ((View) -> Boolean)?) = setOnLongClickListener {
    if (clickValid()) return@setOnLongClickListener listener?.invoke(this) ?: false
    return@setOnLongClickListener false
}

/**
 * 双击事件
 */
@SuppressLint("ClickableViewAccessibility")
fun View.doOnDoubleClick(listener: ((View) -> Unit)?) = setOnTouchListener { v, event ->
    GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent?): Boolean {
            listener?.invoke(v)
            return super.onDoubleTap(e)
        }
    }).onTouchEvent(event)
}

/**
 * 点击事件 多View
 */
fun doOnClick(vararg views: View, listener: ((View) -> Unit)?) {
    views.forEach {
        it.setOnClickListener { view ->
            if (clickValid()) listener?.invoke(view)
        }
    }
}

/**
 * 长按事件 多View
 */
fun doOnLongClick(vararg views: View, listener: ((View) -> Boolean)?) {
    views.forEach {
        it.setOnLongClickListener { view ->
            if (clickValid()) return@setOnLongClickListener listener?.invoke(view) ?: false
            return@setOnLongClickListener false
        }
    }
}

/**
 * 双击事件 多View
 */
@SuppressLint("ClickableViewAccessibility")
fun doOnDoubleClick(vararg views: View, listener: ((View) -> Unit)?) {
    views.forEach {
        it.setOnTouchListener { v, event ->
            GestureDetector(it.context, object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent?): Boolean {
                    listener?.invoke(v)
                    return super.onDoubleTap(e)
                }
            }).onTouchEvent(event)
        }
    }
}

/**
 * View截图返回Bitmap
 */
fun View.toBitmap(): Bitmap {
    val screenshot = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
    val canvas = Canvas(screenshot)
    canvas.translate(-scrollX.toFloat(), -scrollY.toFloat())
    draw(canvas)
    return screenshot
}

fun View.enableElevationOverlay() {
    if (background is MaterialShapeDrawable) return
    var absoluteElevation = 0f
    var viewParent: ViewParent = parent
    while (viewParent is View) {
        absoluteElevation += (viewParent as View).elevation
        viewParent = viewParent.getParent()
    }
    val shapeDrawable = MaterialShapeDrawable.createWithElevationOverlay(context, absoluteElevation)
    background = shapeDrawable
}

fun View.updateElevationOverlay() {
    MaterialShapeUtils.setParentAbsoluteElevation(this)
}