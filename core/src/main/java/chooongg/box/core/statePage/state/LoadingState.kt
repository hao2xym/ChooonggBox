package chooongg.box.core.statePage.state

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import chooongg.box.core.databinding.StatePageLoadingBinding
import chooongg.box.core.statePage.StatePageLayout

class LoadingState : MultiState() {

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

    override fun setVerticalPercentage(percentage: Float) {
        binding.progressCircular.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = percentage
        }
    }

    override fun setHorizontalPercentage(percentage: Float) {
        binding.progressCircular.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = percentage
        }
    }

    override fun setText(text: CharSequence) = Unit

    override fun getText() = ""

    override fun enableReload() = false
}