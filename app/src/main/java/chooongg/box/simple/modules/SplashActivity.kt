package chooongg.box.simple.modules

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import chooongg.box.core.activity.BoxVBActivity
import chooongg.box.ext.getScreenHeight
import chooongg.box.ext.withMain
import chooongg.box.simple.BuildConfig
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivitySplashBinding
import chooongg.box.simple.modules.main.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class SplashActivity : BoxVBActivity<ActivitySplashBinding>() {

    private var job: Job? = null

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        getScreenHeight()
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
//                val oldActivity = ComponentName(applicationContext, SplashActivity::class.java)
//                applicationContext.packageManager.setComponentEnabledSetting(
//                    oldActivity,
//                    PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
//                    PackageManager.DONT_KILL_APP
//                )
//
//                val newActivity =
//                    ComponentName(applicationContext, SplashNewYearActivity::class.java)
//                applicationContext.packageManager.setComponentEnabledSetting(
//                    newActivity,
//                    PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                    PackageManager.DONT_KILL_APP
//                )

                startActivity(Intent(context, MainActivity::class.java))
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out)
            }
        }
    }

    override fun onPause() {
        super.onPause()
        job?.cancel()
    }

    private var firstTime: Long = 0

    override fun onBackPressed() {
        val secondTime = System.currentTimeMillis()
        if (secondTime - firstTime > 2000) {
            Snackbar.make(contentView,"再按一次退出程序",Snackbar.LENGTH_SHORT).show()
            firstTime = secondTime
        } else {
            super.onBackPressed()
        }
    }
}