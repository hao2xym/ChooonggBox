package chooongg.box.simple.modules.loadState

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.core.adapter.BindingItem
import chooongg.box.core.ext.setDefaultNavigation
import chooongg.box.core.statePage.state.LoadingHorizontalState
import chooongg.box.core.statePage.state.LoadingState
import chooongg.box.ext.showToast
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityStatePageBinding
import chooongg.box.simple.databinding.ItemMainBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class StatePageActivity : BoxBindingActivity<ActivityStatePageBinding>() {

    private data class Item(val name: String) : BindingItem<ItemMainBinding>() {
        override val type: Int get() = 0
        override fun bindView(binding: ItemMainBinding, payloads: List<Any>) {
            binding.tvName.text = name
        }
    }

    private val datas = arrayListOf(
        Item("Loading"),
        Item("LoadingHorizontal"),
        Item("Error"),
        Item("Empty"),
        Item("Network"),
        Item("Loading"),
        Item("LoadingHorizontal"),
        Item("Error"),
        Item("Empty"),
        Item("Network"),
        Item("Loading"),
        Item("LoadingHorizontal"),
        Item("Error"),
        Item("Empty"),
        Item("Network"),
        Item("Loading"),
        Item("LoadingHorizontal"),
        Item("Error"),
        Item("Empty"),
        Item("Network")
    )

    private val itemAdapter = ItemAdapter<Item>()

    private val adapter = FastAdapter.with(itemAdapter)

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.boxToolbar.setDefaultNavigation()
        binding.boxToolbar.inflateMenu(R.menu.state_success)
        binding.boxToolbar.setOnMenuItemClickListener {
            binding.statePageLayout.showSuccess()
            false
        }
        binding.recyclerView.adapter = adapter
        itemAdapter.setNewList(datas)
        adapter.onClickListener = { _: View?, _: IAdapter<Item>, item: Item, _: Int ->
            when (item.name) {
                "Loading" -> binding.statePageLayout.show(LoadingState::class)
                "LoadingHorizontal" -> binding.statePageLayout.show(LoadingHorizontalState::class)
                else -> showToast("未实现")
            }
            false
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
}