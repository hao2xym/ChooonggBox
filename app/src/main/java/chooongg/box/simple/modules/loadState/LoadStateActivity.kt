package chooongg.box.simple.modules.loadState

import android.os.Bundle
import chooongg.box.core.activity.BoxVBActivity
import chooongg.box.core.ext.setDefaultNavigation
import chooongg.box.core.statePage.state.LoadingState
import chooongg.box.ext.showToast
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityLoadStateBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class LoadStateActivity : BoxVBActivity<ActivityLoadStateBinding>() {

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
        binding.recyclerView.adapter = Adapter().apply {
            addData(datas)
            setOnItemClickListener { _, _, _ ->
                showToast("切换状态")
                binding.statePageLayout.show(LoadingState::class)
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