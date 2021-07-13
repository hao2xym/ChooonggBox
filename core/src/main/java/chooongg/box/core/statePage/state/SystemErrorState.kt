package chooongg.box.core.statePage.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import chooongg.box.core.databinding.StatePageLoadingBinding
import chooongg.box.core.statePage.StatePageLayout

class SystemErrorState : MultiState() {

    private lateinit var binding: StatePageLoadingBinding

    override fun onCreateMultiStateView(
        context: Context,
        container: StatePageLayout
    ): View {
        binding = StatePageLoadingBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onMultiStateViewCreate(view: View) {
        binding.progressCircular.show()
    }

    override fun setText(text: CharSequence) = Unit

    override fun getText() = ""

    override fun enableReload() = false
}