package chooongg.box.core.loadState.callback

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import chooongg.box.core.R
import chooongg.box.ext.attrColor
import chooongg.box.ext.resourcesColor
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath

class DefaultLoadingCallback : Callback() {

    private lateinit var lottieAnimationView: LottieAnimationView

    override fun getViewLayout() = R.layout.callback_default_loading
    override fun onViewCreated(context: Context, view: View) = Unit
    override fun onAttach(context: Context, view: View) {
        val colorPrimary = context.attrColor(
            R.attr.colorPrimary, context.resourcesColor(R.color.color_primary)
        )
        val colorSecondary = context.attrColor(
            R.attr.colorSecondary, context.resourcesColor(R.color.color_secondary)
        )
        lottieAnimationView = view.findViewById(R.id.lottie_view)
        lottieAnimationView.addValueCallback(KeyPath("Ellipse 1.Ellipse Path 1","Ellipse 1.Stroke 1"), LottieProperty.COLOR, { colorPrimary })
        lottieAnimationView.addValueCallback(KeyPath("Ellipse 1"), LottieProperty.COLOR, { colorSecondary })
        lottieAnimationView.playAnimation()
    }

    override fun setVerticalPercentage(percentage: Float) {
        lottieAnimationView.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = percentage
        }
    }

    override fun setHorizontalPercentage(percentage: Float) {
        lottieAnimationView.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = percentage
        }
    }

    override fun onDetach(context: Context, view: View) {
        lottieAnimationView.cancelAnimation()
    }
}