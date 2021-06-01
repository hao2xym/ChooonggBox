package chooongg.box.core.loadState.callback

import android.content.Context
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import chooongg.box.core.R
import chooongg.box.ext.launchIO
import chooongg.box.ext.withMain
import com.google.android.material.progressindicator.CircularProgressIndicator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class DefaultLoadingCallback : Callback() {

    private lateinit var progressIndicator: CircularProgressIndicator

    private var launch: Job? = null

    override fun getViewLayout() = R.layout.callback_default_loading
    override fun onViewCreated(context: Context, view: View) = Unit
    override fun onAttach(context: Context, view: View) {
        progressIndicator = view.findViewById(R.id.lottie_view)
        launch = GlobalScope.launchIO {
            delay(2000)
            withMain {
                progressIndicator.show()
            }
        }
    }

//    override fun onReloadEvent(): View? {
//        return null
//    }

    override fun setVerticalPercentage(percentage: Float) {
        progressIndicator.updateLayoutParams<ConstraintLayout.LayoutParams> {
            verticalBias = percentage
        }
    }

    override fun setHorizontalPercentage(percentage: Float) {
        progressIndicator.updateLayoutParams<ConstraintLayout.LayoutParams> {
            horizontalBias = percentage
        }
    }

    override fun onDetach(context: Context, view: View) {
        if (launch != null && launch!!.isActive) {
            launch!!.cancel()
            launch = null
        }
        progressIndicator.hide()
    }

    override fun isEnableAnimation() = false
}