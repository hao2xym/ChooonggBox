package chooongg.box.core.statePage.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import chooongg.box.core.R
import chooongg.box.core.databinding.StatePageDefaultBinding
import chooongg.box.core.statePage.StatePageLayout

class EmptyState : MultiState() {

    private lateinit var binding: StatePageDefaultBinding

    override fun onCreateMultiStateView(
        context: Context,
        container: StatePageLayout
    ): View {
        binding = StatePageDefaultBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onMultiStateViewCreate(view: View) {
        binding.ivImage.setImageResource(R.drawable.ic_state_empty)
        binding.tvMessage.text = "没有数据"
    }

    override fun setText(text: CharSequence) {
        binding.tvMessage.text = text
    }

    override fun getText() = ""

    override fun enableReload() = false
}