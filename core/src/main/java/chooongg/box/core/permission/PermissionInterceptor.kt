package chooongg.box.core.permission

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import chooongg.box.core.R
import chooongg.box.ext.showToast
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.hjq.permissions.IPermissionInterceptor
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.util.*

class PermissionInterceptor : IPermissionInterceptor {

    override fun requestPermissions(
        activity: FragmentActivity?,
        callback: OnPermissionCallback?,
        permissions: MutableList<String>?
    ) {
        super.requestPermissions(activity, callback, permissions)
    }

    override fun grantedPermissions(
        activity: FragmentActivity?,
        callback: OnPermissionCallback?,
        permissions: MutableList<String>?,
        all: Boolean
    ) {
        super.grantedPermissions(activity, callback, permissions, all)
    }

    override fun deniedPermissions(
        activity: FragmentActivity?,
        callback: OnPermissionCallback?,
        permissions: MutableList<String>?,
        never: Boolean
    ) {
        // 回调授权失败的方法
        callback!!.onDenied(permissions, never)
        if (never) {
            showPermissionDialog(activity!!, permissions)
            return
        }

        if (permissions!!.size == 1 && Permission.ACCESS_BACKGROUND_LOCATION == permissions[0]) {
            showToast(R.string.common_permission_fail_4, Toast.LENGTH_LONG)
            return
        }
        showToast(R.string.common_permission_fail_1, Toast.LENGTH_LONG)
    }

    /**
     * 显示授权对话框
     */
    protected fun showPermissionDialog(activity: FragmentActivity, permissions: List<String?>?) {
        // 这里的 Dialog 只是示例，没有用 DialogFragment 来处理 Dialog 生命周期
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.common_permission_alert)
            .setCancelable(false)
            .setMessage(getPermissionHint(activity, permissions))
            .setPositiveButton(R.string.common_permission_goto) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
                XXPermissions.startPermissionActivity(activity, permissions)
            }.show()
    }

    /**
     * 根据权限获取提示
     */
    @Suppress("DEPRECATION")
    protected fun getPermissionHint(context: Context, permissions: List<String?>?): String {
        if (permissions == null || permissions.isEmpty()) {
            return context.getString(R.string.common_permission_fail_2)
        }
        val hints: MutableList<String> = ArrayList()
        for (permission in permissions) {
            when (permission) {
                Permission.READ_EXTERNAL_STORAGE,
                Permission.WRITE_EXTERNAL_STORAGE,
                Permission.MANAGE_EXTERNAL_STORAGE -> {
                    val hint = context.getString(R.string.common_permission_storage)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.CAMERA -> {
                    val hint = context.getString(R.string.common_permission_camera)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.RECORD_AUDIO -> {
                    val hint = context.getString(R.string.common_permission_microphone)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.ACCESS_FINE_LOCATION,
                Permission.ACCESS_COARSE_LOCATION,
                Permission.ACCESS_BACKGROUND_LOCATION -> {
                    val hint: String = if (!permissions.contains(Permission.ACCESS_FINE_LOCATION) &&
                        !permissions.contains(Permission.ACCESS_COARSE_LOCATION)
                    ) {
                        context.getString(R.string.common_permission_location_background)
                    } else {
                        context.getString(R.string.common_permission_location)
                    }
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.READ_PHONE_STATE,
                Permission.CALL_PHONE,
                Permission.ADD_VOICEMAIL,
                Permission.USE_SIP,
                Permission.READ_PHONE_NUMBERS,
                Permission.ANSWER_PHONE_CALLS -> {
                    val hint = context.getString(R.string.common_permission_phone)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.GET_ACCOUNTS,
                Permission.READ_CONTACTS,
                Permission.WRITE_CONTACTS -> {
                    val hint = context.getString(R.string.common_permission_contacts)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.READ_CALENDAR,
                Permission.WRITE_CALENDAR -> {
                    val hint = context.getString(R.string.common_permission_calendar)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.READ_CALL_LOG,
                Permission.WRITE_CALL_LOG,
                Permission.PROCESS_OUTGOING_CALLS -> {
                    val hint =
                        context.getString(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) R.string.common_permission_call_log else R.string.common_permission_phone)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.BODY_SENSORS -> {
                    val hint = context.getString(R.string.common_permission_sensors)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.ACTIVITY_RECOGNITION -> {
                    val hint = context.getString(R.string.common_permission_activity_recognition)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.SEND_SMS,
                Permission.RECEIVE_SMS,
                Permission.READ_SMS,
                Permission.RECEIVE_WAP_PUSH,
                Permission.RECEIVE_MMS -> {
                    val hint = context.getString(R.string.common_permission_sms)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.REQUEST_INSTALL_PACKAGES -> {
                    val hint = context.getString(R.string.common_permission_install)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.NOTIFICATION_SERVICE -> {
                    val hint = context.getString(R.string.common_permission_notification)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.SYSTEM_ALERT_WINDOW -> {
                    val hint = context.getString(R.string.common_permission_window)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                Permission.WRITE_SETTINGS -> {
                    val hint = context.getString(R.string.common_permission_setting)
                    if (!hints.contains(hint)) {
                        hints.add(hint)
                    }
                }
                else -> Unit
            }
        }
        if (hints.isNotEmpty()) {
            val builder = StringBuilder()
            for (text in hints) {
                if (builder.isEmpty()) {
                    builder.append(text)
                } else {
                    builder.append("、").append(text)
                }
            }
            builder.append(" ")
            return context.getString(R.string.common_permission_fail_3, builder.toString())
        }
        return context.getString(R.string.common_permission_fail_2)
    }
}