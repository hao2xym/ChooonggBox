package chooongg.box.widget

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.appcompat.widget.AppCompatImageView
import chooongg.box.ext.APP
import chooongg.box.ext.gone
import chooongg.box.ext.visible
import chooongg.box.utils.R
import chooongg.box.utils.ToastUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChooonggToast private constructor(
    private var message: CharSequence?,
    @DrawableRes private var icon: Int? = null,
    @Type private var type: Int = TYPE_UNIVERSAL,
    private var duration: Int = Toast.LENGTH_SHORT
) {

    @IntDef(TYPE_UNIVERSAL, TYPE_EMPHASIZE)
    annotation class Type

    companion object {
        const val TYPE_UNIVERSAL = 0
        const val TYPE_EMPHASIZE = 1

        fun make(
            message: CharSequence?,
            @DrawableRes icon: Int?,
            @Type type: Int,
            duration: Int
        ) = ChooonggToast(message, icon, type, duration)
    }

    private var gravity = Gravity.CENTER
    private var xOffset = 0
    private var yOffset = 0
    private var backgroundColor = -1
    private var backgroundImage = -1
    private var contentColor = -1

    fun setIcon(@DrawableRes icon: Int) = apply {
        this.icon = icon
    }

    fun setType(@Type type: Int) = apply {
        this.type = type
    }

    fun setDuration(duration: Int) = apply {
        this.duration = duration
    }

    fun setGravity(gravity: Int, xOffset: Int = 0, yOffset: Int = 0) = apply {
        this.gravity = gravity
        this.xOffset = xOffset
        this.yOffset = yOffset
    }

    fun setBackgroundColor(@ColorInt color: Int) = apply {
        this.backgroundColor = color
    }

    fun setBackgroundImage(@DrawableRes resId: Int) = apply {
        this.backgroundImage = resId
    }

    fun setContentColor(@ColorInt color: Int) = apply {
        this.contentColor = color
    }

    fun show() {
        GlobalScope.launch(Dispatchers.Main) {
            ToastUtils.cancel()
            val layoutResId = when (type) {
                TYPE_EMPHASIZE -> R.layout.toast_emphasize
                else -> R.layout.toast_universal
            }
            if (message.isNullOrEmpty() && icon == -1) return@launch
            ToastUtils.chooonggToast = Toast.makeText(APP, message, duration).apply {
                val contentView = LayoutInflater.from(APP).inflate(layoutResId, null)
                contentView.findViewById<TextView>(R.id.tv_text).apply {
                    if (message.isNullOrEmpty()) {
                        gone()
                    } else {
                        this.text = message
                        if (contentColor != -1) setTextColor(contentColor)
                        visible()
                    }
                }
                contentView.findViewById<AppCompatImageView>(R.id.iv_image).apply {
                    if (icon != null) {
                        setImageResource(icon!!)
                        if (contentColor != -1) setColorFilter(contentColor)
                        visible()
                    } else {
                        gone()
                    }
                }
                if (backgroundImage != -1) {
                    contentView.setBackgroundResource(backgroundImage)
                }
                if (backgroundColor != -1 && contentView.background is GradientDrawable) {
                    (contentView.background as GradientDrawable).setColor(backgroundColor)
                }
                view = contentView

            }
            if (gravity != Gravity.NO_GRAVITY) ToastUtils.chooonggToast!!.setGravity(gravity, xOffset, yOffset)
            ToastUtils.chooonggToast!!.show()
        }
    }
}