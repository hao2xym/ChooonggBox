package chooongg.box.core.widget.dropDownMenu

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.HorizontalScrollView
import androidx.appcompat.widget.LinearLayoutCompat
import chooongg.box.core.R
import chooongg.box.core.widget.dropDownMenu.model.DropDownMenuItem

class DropDownMenuView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    private val linearLayout: LinearLayoutCompat = LinearLayoutCompat(context).apply {
        gravity = Gravity.CENTER_HORIZONTAL
    }

    private val views = ArrayList<View>()

    private var menu = ArrayList<DropDownMenuItem>()

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DropDownMenuView)

        a.recycle()
    }

    fun setMenu(menu: ArrayList<DropDownMenuItem>) {
        linearLayout.removeAllViews()
        this.menu = menu
        this.menu.sort()
    }

    private fun createMenuItem() {

    }
}