package chooongg.box.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.annotation.DimenRes
import chooongg.box.core.R
import chooongg.box.core.ext.setDefaultNavigation
import chooongg.box.ext.getActivity
import chooongg.box.ext.loadActivityLabel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.shape.MaterialShapeDrawable
import com.google.android.material.shape.MaterialShapeUtils

@SuppressLint("CustomViewStyleable")
class BoxToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.toolbarStyle,
    defStyleRes: Int = R.style.BoxWidget_Toolbar_PrimarySurface
) : MaterialToolbar(context, attrs, defStyleAttr) {

    private val syncStatusBarColor: Boolean

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.BoxToolBar, defStyleAttr, defStyleRes)
        if (a.getBoolean(R.styleable.BoxToolBar_loadActivityLabel, false)) {
            title = context.loadActivityLabel()
        }
        if (a.getBoolean(R.styleable.BoxToolBar_defaultNavigation, false)) {
            setDefaultNavigation()
        }
        syncStatusBarColor = a.getBoolean(R.styleable.BoxToolBar_syncStatusBarColor, true)
        a.recycle()
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
        if (syncStatusBarColor) {
            when (background) {
                is MaterialShapeDrawable -> {
                    val elevationOverlayProvider = ElevationOverlayProvider(context)
                    val color = elevationOverlayProvider.compositeOverlayIfNeeded(
                        background.fillColor?.defaultColor
                            ?: elevationOverlayProvider.themeSurfaceColor, elevation
                    )
                    if (Color.alpha(color) == 255) {
                        context.getActivity()?.window?.statusBarColor = color
                    }
                }
                is ColorDrawable -> {
                    if (Color.alpha(background.color) == 255) {
                        context.getActivity()?.window?.statusBarColor = background.color
                    }
                }
            }
        }
    }

    /**
     * 适配黑夜模式的高度覆盖层模式
     */
    fun setOElevationOverlayMode(elevation: Float) {
        this.elevation = 0f
        MaterialShapeUtils.setElevation(this, elevation)
    }

    /**
     * 适配黑夜模式的高度覆盖层模式
     */
    fun setOElevationOverlayMode(@DimenRes resId: Int) {
        this.elevation = 0f
        MaterialShapeUtils.setElevation(this, resources.getDimension(resId))
    }
}