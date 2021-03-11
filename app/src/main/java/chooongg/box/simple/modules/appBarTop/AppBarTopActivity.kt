package chooongg.box.simple.modules.appBarTop

import android.os.Bundle
import chooongg.box.core.activity.BoxVBActivity
import chooongg.box.ext.decorView
import chooongg.box.simple.databinding.ActivityMainBinding
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class AppBarTopActivity : BoxVBActivity<ActivityMainBinding>() {

    override fun initConfig(savedInstanceState: Bundle?) {
        decorView.transitionName = "shared_element_end_root"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform(this, true)
        window.sharedElementExitTransition = MaterialContainerTransform(this, false)
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}