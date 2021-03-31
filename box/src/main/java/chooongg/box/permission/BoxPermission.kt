package chooongg.box.permission

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import chooongg.box.permission.request.PermissionBuilder
import chooongg.box.permission.request.RequestBackgroundLocationPermission

object BoxPermission {

    /**
     * 维护我们需要通过特殊情况处理的所有特殊权限
     */
    @TargetApi(Build.VERSION_CODES.R)
    val allSpecialPermissions = setOf(
        Manifest.permission.ACCESS_BACKGROUND_LOCATION,
        Manifest.permission.SYSTEM_ALERT_WINDOW,
        Manifest.permission.WRITE_SETTINGS,
        Manifest.permission.MANAGE_EXTERNAL_STORAGE
    )

    /**
     * 是否被授予该权限
     */
    fun isGranted(context: Context, permission: String) = ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED

    class BoxPermissionMediator {

        private val fragment: Fragment?
        private val activity: FragmentActivity?

        constructor(activity: FragmentActivity) {
            this.activity = activity
            this.fragment = null
        }

        constructor(fragment: Fragment) {
            this.activity = null
            this.fragment = fragment
        }

        fun permissions(vararg permissions: String): PermissionBuilder {
            val normalPermissionSet = LinkedHashSet<String>()
            val specialPermissionSet = LinkedHashSet<String>()
            val osVersion = Build.VERSION.SDK_INT
            val targetSdkVersion = activity?.applicationInfo?.targetSdkVersion
                ?: fragment!!.requireContext().applicationInfo.targetSdkVersion
            for (permission in permissions) {
                if (permission in allSpecialPermissions) {
                    specialPermissionSet.add(permission)
                } else {
                    normalPermissionSet.add(permission)
                }
            }
            if (RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION in specialPermissionSet) {
                if (osVersion == Build.VERSION_CODES.Q ||
                    (osVersion == Build.VERSION_CODES.R && targetSdkVersion < Build.VERSION_CODES.R)
                ) {
                    // If we request ACCESS_BACKGROUND_LOCATION on Q or on R but targetSdkVersion below R,
                    // We don't need to request specially, just request as normal permission.
                    specialPermissionSet.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    normalPermissionSet.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                }
            }
            return PermissionBuilder(
                activity ?: fragment?.activity,
                fragment,
                normalPermissionSet,
                specialPermissionSet
            )
        }
    }
}