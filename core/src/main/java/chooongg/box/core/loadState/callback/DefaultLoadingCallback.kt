package chooongg.box.core.loadState.callback

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import chooongg.box.core.databinding.CallbackDefaultLoadingBinding
import chooongg.box.ext.layoutInflater

class DefaultLoadingCallback : Callback() {

    private lateinit var binding: CallbackDefaultLoadingBinding

    override fun onBuildView(context: Context): View {
        binding = CallbackDefaultLoadingBinding.inflate(context.layoutInflater)
        return binding.root
    }

    override fun onViewCreated(context: Context, view: View) = Unit
    override fun onAttach(context: Context, view: View) {
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

    override fun onDetach(context: Context, view: View) {
        binding.progressCircular.hide()
    }
}