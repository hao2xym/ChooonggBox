package chooongg.box.core.statePage.state

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import chooongg.box.core.databinding.StatePageLoadingHorizontalBinding
import chooongg.box.core.statePage.StatePageLayout

class LoadingHorizontalState : MultiState() {

    private lateinit var binding: StatePageLoadingHorizontalBinding

    override fun onCreateMultiStateView(
        context: Context,
        container: StatePageLayout
    ): View {
        binding = StatePageLoadingHorizontalBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onMultiStateViewCreate(view: View) {
        binding.progressHorizontal.show()
    }

    override fun isShowSuccessView() = true

    override fun setText(text: CharSequence) = Unit

    override fun getText() = ""

    override fun enableReload() = false
}