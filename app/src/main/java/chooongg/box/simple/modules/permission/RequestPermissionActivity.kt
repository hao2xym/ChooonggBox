package chooongg.box.simple.modules.permission

import android.os.Bundle
import android.view.View
import chooongg.box.core.activity.BoxVBActivity
import chooongg.box.core.adapter.BindingItem
import chooongg.box.core.adapter.BoxAdapter
import chooongg.box.core.adapter.VBHolder
import chooongg.box.ext.showToast
import chooongg.box.simple.databinding.ActivityPermissionBinding
import chooongg.box.simple.databinding.ItemMainBinding
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class RequestPermissionActivity : BoxVBActivity<ActivityPermissionBinding>() {

    private data class Entity(val name: String) : BindingItem<ItemMainBinding>() {
        override val type: Int get() = 0
        override fun bindView(binding: ItemMainBinding, payloads: List<Any>) {
            binding.tvName.text = name
        }
    }

    private val datas = arrayListOf(
        Entity("请求单权限"),
        Entity("请求多权限"),
        Entity("请求权限前提示说明")
    )

    private val itemAdapter = ItemAdapter.items<GenericItem>()
    private val adapter = FastAdapter.with(itemAdapter)

    override fun initConfig(savedInstanceState: Bundle?) {
        val itemAdapter = ItemAdapter<Entity>()
        val adapter = FastAdapter.with(itemAdapter)
        itemAdapter.add(datas)
        adapter.onClickListener = { v: View?, _: IAdapter<Entity>, item: Entity, position: Int ->
            showToast(item.name)
            false
        }
        binding.recyclerView.adapter = adapter
//        binding.recyclerView.adapter = Adapter().apply {
//            setDiffNewData(datas)
//            setOnItemClickListener { _, _, position ->
//
//            }
//        }
    }

    override fun initContent(savedInstanceState: Bundle?) {

    }

    private class Adapter : BoxAdapter<String, ItemMainBinding>() {
        override fun convert(holder: VBHolder<ItemMainBinding>, item: String) {

        }
    }
}