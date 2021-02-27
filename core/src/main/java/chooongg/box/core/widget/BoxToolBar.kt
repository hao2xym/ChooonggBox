package chooongg.box.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import com.google.android.material.R
import com.google.android.material.appbar.MaterialToolbar

@SuppressLint("CustomViewStyleable")
class BoxToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : MaterialToolbar(context, attrs, defStyleAttr) {

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.Toolbar,
            defStyleAttr,
            R.style.Widget_MaterialComponents_Toolbar
        )
        a.getResourceId()
        a.recycle()
    }

}