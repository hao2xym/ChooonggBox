package chooongg.box.simple.modules.permission

import android.os.Bundle
import android.view.View
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.core.adapter.BindingItem
import chooongg.box.ext.showToast
import chooongg.box.simple.databinding.ActivityPermissionBinding
import chooongg.box.simple.databinding.ItemMainBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.GenericItem
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class RequestPermissionActivity : BoxBindingActivity<ActivityPermissionBinding>() {

    private data class Operate(val name: String) : BindingItem<ItemMainBinding>() {
        override val type: Int get() = 0
        override fun bindView(binding: ItemMainBinding, payloads: List<Any>) {
            binding.tvName.text = name
        }
    }

    private val datas = arrayListOf(
        Operate("请求单权限"),
        Operate("请求多权限")
    )

    private val itemAdapter = ItemAdapter.items<GenericItem>()
    private val adapter = FastAdapter.with(itemAdapter)

    override fun initConfig(savedInstanceState: Bundle?) {
        itemAdapter.add(datas)
        adapter.onClickListener =
            { _: View?, _: IAdapter<GenericItem>, item: GenericItem, _: Int ->
                if (item is Operate) {
                    when (item.name) {
                        "请求单权限" -> {
                            XXPermissions.with(this)
                                .permission(Permission.CAMERA)
                                .request(object : OnPermissionCallback {
                                    override fun onGranted(
                                        permissions: MutableList<String>?,
                                        all: Boolean
                                    ) {
                                        showToast("相机权限申请成功")
                                    }

                                    override fun onDenied(
                                        permissions: MutableList<String>?,
                                        never: Boolean
                                    ) {
                                        showToast("相机权限申请失败")
                                    }
                                })
                        }
                        "请求多权限" -> {
                            XXPermissions.with(this)
                                .permission(
                                    Permission.ACCESS_FINE_LOCATION,
                                    Permission.ACCESS_COARSE_LOCATION,
                                    Permission.ACCESS_BACKGROUND_LOCATION,
                                )
                                .request(object : OnPermissionCallback {
                                    override fun onGranted(
                                        permissions: MutableList<String>?,
                                        all: Boolean
                                    ) {
                                        showToast("相机权限申请成功")
                                    }

                                    override fun onDenied(
                                        permissions: MutableList<String>?,
                                        never: Boolean
                                    ) {
                                        showToast("相机权限申请失败")
                                    }
                                })
                        }
                    }
                }
                false
            }
        binding.recyclerView.adapter = adapter
    }

    override fun initContent(savedInstanceState: Bundle?) {

    }
}