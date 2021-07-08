package chooongg.box.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import chooongg.box.core.R
import chooongg.box.core.ext.setDefaultNavigation
import chooongg.box.ext.getActivity
import chooongg.box.ext.loadActivityLabel
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.elevation.ElevationOverlayProvider
import com.google.android.material.shape.MaterialShapeDrawable

@SuppressLint("CustomViewStyleable")
class BoxToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.toolbarStyle,
    defStyleRes: Int = R.style.BoxWidget_Toolbar_PrimarySurface
) : MaterialToolbar(context, attrs, defStyleAttr) {

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.BoxToolBar, defStyleAttr, defStyleRes)
        if (a.getBoolean(R.styleable.BoxToolBar_defaultNavigation, false)) {
            setDefaultNavigation()
        }
        if (a.getBoolean(R.styleable.BoxToolBar_loadActivityLabel, false)) {
            val string = context.loadActivityLabel()
            title = string
        }
        a.recycle()
    }

    override fun setBackground(background: Drawable?) {
        super.setBackground(background)
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