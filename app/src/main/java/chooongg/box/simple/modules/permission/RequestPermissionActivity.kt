package chooongg.box.simple.modules.permission

import android.os.Bundle
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.simple.databinding.ActivityPermissionBinding

class RequestPermissionActivity : BoxBindingActivity<ActivityPermissionBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
//        itemAdapter.add(datas)
//        adapter.onClickListener =
//            { _: View?, _: IAdapter<GenericItem>, item: GenericItem, _: Int ->
//                if (item is Operate) {
//                    when (item.name) {
//                        "请求单权限" -> {
//                            XXPermissions.with(this)
//                                .permission(Permission.CAMERA)
//                                .request(object : OnPermissionCallback {
//                                    override fun onGranted(
//                                        permissions: MutableList<String>?,
//                                        all: Boolean
//                                    ) {
//                                        showToast("相机权限申请成功")
//                                    }
//
//                                    override fun onDenied(
//                                        permissions: MutableList<String>?,
//                                        never: Boolean
//                                    ) {
//                                        showToast("相机权限申请失败")
//                                    }
//                                })
//                        }
//                        "请求多权限" -> {
//                            XXPermissions.with(this)
//                                .permission(
//                                    Permission.ACCESS_FINE_LOCATION,
//                                    Permission.ACCESS_COARSE_LOCATION,
//                                    Permission.ACCESS_BACKGROUND_LOCATION,
//                                )
//                                .request(object : OnPermissionCallback {
//                                    override fun onGranted(
//                                        permissions: MutableList<String>?,
//                                        all: Boolean
//                                    ) {
//                                        showToast("相机权限申请成功")
//                                    }
//
//                                    override fun onDenied(
//                                        permissions: MutableList<String>?,
//                                        never: Boolean
//                                    ) {
//                                        showToast("相机权限申请失败")
//                                    }
//                                })
//                        }
//                    }
//                }
//                false
//            }
//        binding.recyclerView.adapter = adapter
    }

    override fun initContent(savedInstanceState: Bundle?) {

    }
}