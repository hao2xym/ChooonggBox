package chooongg.box.simple.modules.loadState

import android.os.Bundle
import chooongg.box.core.activity.BoxVBActivity
import chooongg.box.core.loadState.LoadService
import chooongg.box.core.loadState.LoadUtils
import chooongg.box.core.loadState.callback.DefaultLoadingCallback
import chooongg.box.core.loadState.callback.SuccessCallback
import chooongg.box.ext.doOnClick
import chooongg.box.simple.databinding.ActivityLoadStateBinding

class LoadStateActivity : BoxVBActivity<ActivityLoadStateBinding>() {

    private lateinit var loadService: LoadService<*>

    override fun initConfig(savedInstanceState: Bundle?) {
        loadService = LoadUtils.Builder()
            .addCallback(DefaultLoadingCallback::class)
            .setDefaultCallback(SuccessCallback::class)
            .build().register(binding.scrollView) {
                loadService.showSuccess()
            }
    }

    override fun initContent(savedInstanceState: Bundle?) {
        binding.tvTest.doOnClick {
            loadService.showCallback(DefaultLoadingCallback::class)
        }
    }
}