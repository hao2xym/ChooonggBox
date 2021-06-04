package chooongg.box.simple.modules.loadState

import android.os.Bundle
import chooongg.box.core.activity.BoxVBActivity
import chooongg.box.core.ext.setDefaultNavigation
import chooongg.box.core.loadState.LoadService
import chooongg.box.core.loadState.LoadUtils
import chooongg.box.core.loadState.callback.DefaultLoadingCallback
import chooongg.box.core.loadState.callback.SuccessCallback
import chooongg.box.log.BoxLog
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityLoadStateBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class LoadStateActivity : BoxVBActivity<ActivityLoadStateBinding>() {

    private lateinit var loadService: LoadService<*>

    private val datas = arrayListOf(
        "加载中",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表",
        "测试列表"
    )

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.boxToolbar.setDefaultNavigation()
        loadService = LoadUtils.Builder()
            .addCallback(DefaultLoadingCallback::class)
            .setDefaultCallback(SuccessCallback::class)
            .build().register(binding.refreshLayout) {
                BoxLog.e("点击")
                loadService.showSuccess()
            }
        binding.recyclerView.adapter = Adapter().apply {
            addData(datas)
            setOnItemClickListener { _, _, _ ->
                loadService.showCallback(DefaultLoadingCallback::class)
            }
        }
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }

    private class Adapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_name, item)
        }
    }
}