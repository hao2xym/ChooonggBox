package chooongg.box.simple.modules.appBarTop

import android.os.Bundle
import android.transition.Slide
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.simple.databinding.ActivityTopAppbarBinding

class TopAppBarActivity : BoxBindingActivity<ActivityTopAppbarBinding>() {

    override fun initTransition() {
        contentView.transitionName = "root"
        window.enterTransition = Slide()
        window.exitTransition = Slide()
    }

    override fun initConfig(savedInstanceState: Bundle?) {

    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}