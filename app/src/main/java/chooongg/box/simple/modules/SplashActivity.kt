package chooongg.box.simple.modules

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import chooongg.box.core.activity.BoxVBActivity
import chooongg.box.ext.withMain
import chooongg.box.simple.BuildConfig
import chooongg.box.simple.databinding.ActivitySplashBinding
import chooongg.box.simple.modules.main.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BoxVBActivity<ActivitySplashBinding>() {

    private var job: Job? = null

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.tvVersion.text = BuildConfig.VERSION_NAME
    }

    override fun initContent(savedInstanceState: Bundle?) {
        binding.ivLogo.animate().alpha(1f)
        binding.tvName.animate().alpha(1f).translationY(0f)
    }

    override fun onResume() {
        super.onResume()
        job = lifecycleScope.launch {
            delay(3000)
            withMain {
                startActivity(
                    Intent(context, MainActivity::class.java),
                    ActivityOptions.makeSceneTransitionAnimation(activity).toBundle()
                )
            }
        }
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }
}