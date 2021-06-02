package chooongg.box.core.ext

import androidx.appcompat.widget.Toolbar
import chooongg.box.core.R
import chooongg.box.ext.getActivity

fun Toolbar.setDefaultNavigation() {
    setNavigationIcon(R.drawable.ic_app_bar_back)
    setNavigationOnClickListener {
        context.getActivity()?.onBackPressed()
    }
}