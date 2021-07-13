package chooongg.box.simple.modules.loadState

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.constraintlayout.motion.widget.MotionLayout
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.core.ext.setDefaultNavigation
import chooongg.box.core.statePage.state.*
import chooongg.box.ext.showToast
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityStatePageBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

class StatePageActivity : BoxBindingActivity<ActivityStatePageBinding>() {

    private val datas = arrayListOf(
        "Loading",
        "LoadingHorizontal",
        "Error",
        "Empty",
        "Network",
        "Loading",
        "LoadingHorizontal",
        "Error",
        "Empty",
        "Network",
        "Loading",
        "LoadingHorizontal",
        "Error",
        "Empty",
        "Network",
        "Loading",
        "LoadingHorizontal",
        "Error",
        "Empty",
        "Network"
    )

    private val adapter = Adapter()

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.boxToolbar.setDefaultNavigation()
        binding.boxToolbar.inflateMenu(R.menu.state_success)
        binding.boxToolbar.setOnMenuItemClickListener {
            binding.statePageLayout.showSuccess()
            false
        }
        binding.recyclerView.adapter = adapter
        adapter.setNewInstance(datas)
        adapter.setOnItemClickListener { _, _, position ->
            when (datas[position]) {
                "Loading" -> binding.statePageLayout.show(LoadingState::class)
                "LoadingHorizontal" -> binding.statePageLayout.show(LoadingHorizontalState::class)
                "Error" -> binding.statePageLayout.show(ErrorState::class)
                "Empty" -> binding.statePageLayout.show(EmptyState::class)
                "Network" -> binding.statePageLayout.show(NetworkState::class)
                else -> showToast("未实现")
            }
        }
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) = Unit
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) =
                Unit

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                setBackground()
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                setBackground()
            }

            fun setBackground() {
                val background = binding.toolbarLayout.background
                if (background is ColorDrawable) {
                    window.statusBarColor = background.color
                }
            }
        })
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }

    private class Adapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_main) {
        override fun convert(holder: BaseViewHolder, item: String) {
            holder.setText(R.id.tv_name, item)
        }
    }
}