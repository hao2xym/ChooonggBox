package chooongg.box.simple.modules.permission

import android.os.Bundle
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.ext.showToast
import chooongg.box.simple.databinding.ActivityPermissionBinding
import chooongg.box.simple.modules.main.MainActivity
import chooongg.box.simple.modules.main.entity.MainItemEntity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class RequestPermissionActivity : BoxBindingActivity<ActivityPermissionBinding>() {

    private val adapter = MainActivity.Adapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        adapter.setNewInstance(
            arrayListOf(
                MainItemEntity("请求单权限"),
                MainItemEntity("请求多权限"),
                MainItemEntity("请求单权限"),
                MainItemEntity("请求多权限")
            )
        )
        adapter.setOnItemClickListener { _, _, position ->
            when (adapter.data[position].name) {
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
    }

    override fun initContent(savedInstanceState: Bundle?) {

    }
}