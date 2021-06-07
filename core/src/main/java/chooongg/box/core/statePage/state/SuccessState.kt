package chooongg.box.core.statePage.state

import android.content.Context
import android.view.View
import chooongg.box.core.statePage.StatePageLayout

class SuccessState : MultiState() {

    override fun onCreateMultiStateView(
        context: Context,
        container: StatePageLayout
    ): View {
        return View(context)
    }

    override fun onMultiStateViewCreate(view: View) = Unit

    override fun setText(text: CharSequence) = Unit

    override fun getText() = ""

    override fun enableReload() = false

}