package chooongg.box.core.permission

import android.Manifest
import android.annotation.TargetApi
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import chooongg.box.core.permission.request.*

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

    @JvmStatic
    fun init(activity: FragmentActivity) = PermissionMediator(activity)

    @JvmStatic
    fun init(fragment: Fragment) = PermissionMediator(fragment)

    /**
     * 检查权限是否被授予
     *
     * @param context    任何 Context
     * @param permission 具体要检查的权限名称
     * @return 如果授予此权限，则为True，否则为False
     */
    fun isGranted(context: Context?, permission: String?): Boolean {
        return ContextCompat.checkSelfPermission(context!!,
            permission!!) == PackageManager.PERMISSION_GRANTED
    }

    interface RequestCallback {
        /**
         * 回调请求结果
         * @param allGranted 是否授予了所有权限
         * @param grantedList 用户授予的权限
         * @param deniedList 用户拒绝的所有权限
         */
        fun onResult(allGranted: Boolean, grantedList: List<String?>?, deniedList: List<String?>?)
    }

    interface ExplainReasonCallback {
        /**
         * 在你应解释为什么需要这些权限时调用
         * @param scope 显示基本原理对话框
         * @param deniedList 应该解释的权限
         */
        fun onExplainReason(scope: ExplainScope?, deniedList: List<String?>?)
    }

    interface ExplainReasonCallbackWithBeforeParam {
        /**
         * 在您应解释为什么需要这些权限时调用
         * @param scope 显示基本原理对话框
         * @param deniedList 应该解释的权限
         * @param beforeRequest 是否在请求前
         */
        fun onExplainReason(
            scope: ExplainScope?,
            deniedList: List<String?>?,
            beforeRequest: Boolean,
        )
    }

    interface ForwardToSettingsCallback {
        /**
         * 在告诉用户在设置中允许这些权限时调用
         * @param scope 显示基本原理对话框
         * @param deniedList 设置中应允许的权限
         */
        fun onForwardToSettings(scope: ForwardScope?, deniedList: List<String?>?)
    }

    class PermissionMediator {
        private var activity: FragmentActivity? = null
        private var fragment: Fragment? = null

        internal constructor(activity: FragmentActivity) {
            this.activity = activity
        }

        internal constructor(fragment: Fragment) {
            this.activity = fragment.activity
            this.fragment = fragment
        }

        fun permission(permissions: List<String>): PermissionBuilder {
            val normalPermissionSet = LinkedHashSet<String>()
            val specialPermissionSet = LinkedHashSet<String>()
            val osVersion = Build.VERSION.SDK_INT
            val targetSdkVersion = if (activity != null) {
                activity!!.applicationInfo.targetSdkVersion
            } else {
                fragment!!.requireContext().applicationInfo.targetSdkVersion
            }
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
            return PermissionBuilder(activity, fragment, normalPermissionSet, specialPermissionSet)
        }
    }

    class PermissionBuilder(
        internal val activity: FragmentActivity?,
        internal val fragment: Fragment?,
        internal val normalPermissions: Set<String>,
        internal val specialPermissions: Set<String>,
    ) {

        /**
         * 向用户显示的当前对话框的实例
         * 当InvisibleFragment销毁时，我们需要关闭此对话框
         */
        internal var currentDialog: Dialog? = null

        /**
         * 在请求权限之前解释提示请求的原因
         */
        internal var explainReasonBeforeRequest = false

        /**
         * 显示对话框调用
         * {@link ExplainScope#showRequestReasonDialog(List, String, String)}
         * {@link ForwardScope#showForwardToSettingsDialog(List, String, String)}
         * {@link #onExplainRequestReason(ExplainReasonCallback)}
         * {@link #onForwardToSettings(ForwardToSettingsCallback)}
         */
        internal var showDialogCalled = false

        /**
         * 一些不应请求的权限将存储在此处
         * 并在请求完成时通知用户
         */
        internal var permissionsWontRequest: LinkedHashSet<String> = LinkedHashSet()

        /**
         * 在请求的权限中授予的权限
         */
        internal var grantedPermissions: LinkedHashSet<String> = LinkedHashSet()

        /**
         * 在请求的权限中被拒绝的权限
         */
        internal var deniedPermissions: LinkedHashSet<String> = LinkedHashSet()

        /**
         * 保留在请求的权限中被永久拒绝的权限
         * {@link 拒绝，不再提示}
         */
        internal var permanentDeniedPermissions: LinkedHashSet<String> = LinkedHashSet()

        /**
         * 当我们请求多个权限时，有些被拒绝，有些被永久拒绝
         * 拒绝的权限将首先回调。并且永久拒绝的权限将存储在此tempPermanentDeniedPermissions中
         * 一旦不再存在拒绝的权限，它们将被回调
         */
        internal var tempPermanentDeniedPermissions: LinkedHashSet<String> = LinkedHashSet()

        /**
         * 拥有应转发给“设置”以允许它们的权限。并非所有永久拒绝的权限都应转发到“设置”
         * 只有那些认为必要的开发人员才应该这样做
         */
        internal var forwardPermissions: LinkedHashSet<String> = LinkedHashSet()

        /**
         * 权限请求回调
         */
        internal var requestCallback: RequestCallback? = null

        /**
         * 解释原因回调
         */
        internal var explainReasonCallback: ExplainReasonCallback? = null

        /**
         * 请求前解释原因回调
         */
        internal var explainReasonCallbackWithBeforeParam: ExplainReasonCallbackWithBeforeParam? =
            null

        /**
         * 跳转权限设置回调
         */
        internal var forwardToSettingsCallback: ForwardToSettingsCallback? = null

        /**
         * 在权限需要说明请求原因时调用
         * 通常，每次用户拒绝您的请求时，都会调用此方法
         * 如果设置[.explainReasonBeforeRequest]，则此方法可能在权限请求之前运行
         */
        fun onExplainRequestReason(callback: ExplainReasonCallback) = apply {
            explainReasonCallback = callback
        }

        /**
         * 在权限需要说明请求原因时调用
         * 通常，每次用户拒绝您的请求时，都会调用此方法
         * 如果设置[.explainReasonBeforeRequest]，则此方法可能在权限请求之前运行
         * beforeRequest参数会告诉您此方法当前在权限请求之前或之后
         */
        fun onExplainRequestReason(callback: ExplainReasonCallbackWithBeforeParam) = apply {
            explainReasonCallbackWithBeforeParam = callback
        }

        /**
         * 当权限需要转发到“设置”以允许时调用
         * 通常，用户会拒绝您的请求，并且选中“不再询问”会调用此方法
         * 请记住，[.onExplainRequestReason]始终在此方法之前。如果调用[.onExplainRequestReason]，则不会在相同的请求时间内调用此方法
         */
        fun onForwardToSettings(callback: ForwardToSettingsCallback) = apply {
            forwardToSettingsCallback = callback
        }

        /**
         * 如果您需要显示请求许可的基本原理
         * 请在您的请求语法中链接此方法
         * [.onExplainRequestReason] 将在权限请求之前被调用
         */
        fun explainReasonBeforeRequest() = apply { explainReasonBeforeRequest = true }

        /**
         * 立即请求权限，并在回调中处理请求
         */
        fun request(callback: RequestCallback) {
            requestCallback = callback
            // 建立请求链
            val requestChain = RequestChain()
            requestChain.addTaskToChain(RequestNormalPermissions(this))
            requestChain.addTaskToChain(RequestBackgroundLocationPermission(this))
            requestChain.addTaskToChain(RequestSystemAlertWindowPermission(this))
            requestChain.addTaskToChain(RequestWriteSettingsPermission(this))
            requestChain.addTaskToChain(RequestManageExternalStoragePermission(this))
            requestChain.runTask()
        }

        /**
         * 此方法是内部的，开发人员不应调用此方法
         * 向用户显示一个对话框，并说明为什么需要这些权限
         *
         * @param chainTask              当前任务的实例
         * @param showReasonOrGoSettings 指示应显示解释原因或转至设置
         * @param permissions            再次请求的权限
         * @param message                向用户解释为什么需要这些权限的信息
         * @param positiveText           再次请求的积极的按钮上的正面文字
         * @param negativeText           否定按钮上的否定文字。如果不应该取消此对话框，则为null
         */
        fun showHandlePermissionDialog(
            chainTask: ChainTask,
            showReasonOrGoSettings: Boolean,
            permissions: List<String?>?,
            message: String?,
            positiveText: String?,
            negativeText: String?,
        ) {
//            val defaultDialog = DefaultDialog(activity,
//                permissions,
//                message,
//                positiveText,
//                negativeText,
//                lightColor,
//                darkColor)
//            showHandlePermissionDialog(chainTask, showReasonOrGoSettings, defaultDialog)
        }

        /**
         * 此方法是内部的，开发人员不应调用此方法
         * 向用户显示一个对话框，并说明为什么需要这些权限
         *
         * @param chainTask              当前任务的实例
         * @param showReasonOrGoSettings 指示应显示解释原因或转至设置
         * @param dialog                 弹框，向用户解释为什么需要这些权限
         */
        fun showHandlePermissionDialog(
            chainTask: ChainTask,
            showReasonOrGoSettings: Boolean,
            dialog: Dialog,/*RationaleDialog*/
        ) {
//            showDialogCalled = true
//            val permissions: List<String?> = dialog.getPermissionsToRequest()
//            if (permissions.isEmpty()) {
//                chainTask.finish()
//                return
//            }
//            currentDialog = dialog
//            dialog.show()
//            val positiveButton: View? = dialog.getPositiveButton()
//            val negativeButton: View? = dialog.getNegativeButton()
//            dialog.setCancelable(false)
//            dialog.setCanceledOnTouchOutside(false)
//            positiveButton.isClickable = true
//            positiveButton.setOnClickListener {
//                dialog.dismiss()
//                if (showReasonOrGoSettings) {
//                    chainTask.requestAgain(permissions)
//                } else {
//                    forwardToSettings(permissions)
//                }
//            }
//            if (negativeButton != null) {
//                negativeButton.isClickable = true
//                negativeButton.setOnClickListener {
//                    dialog.dismiss()
//                    chainTask.finish()
//                }
//            }
//            currentDialog!!.setOnDismissListener { currentDialog = null }
        }

        /**
         * 此方法是内部的，开发人员不应调用此方法
         * 向用户显示一个对话框，并说明为什么需要这些权限
         *
         * @param chainTask              当前任务的实例
         * @param showReasonOrGoSettings 指示应显示解释原因或转至设置
         * @param dialogFragment         弹框碎片，向用户解释为什么需要这些权限
         */
        fun showHandlePermissionDialog(
            chainTask: ChainTask,
            showReasonOrGoSettings: Boolean,
            dialogFragment: RationaleDialogFragment,
        ) {
            showDialogCalled = true
            val permissions: List<String?> = dialogFragment.getPermissionsToRequest()
            if (permissions.isEmpty()) {
                chainTask.finish()
                return
            }
            dialogFragment.showNow(getFragmentManager(), "PermissionXRationaleDialogFragment")
            val positiveButton: View = dialogFragment.getPositiveButton()
            val negativeButton: View = dialogFragment.getNegativeButton()
            dialogFragment.setCancelable(false)
            positiveButton.isClickable = true
            positiveButton.setOnClickListener {
                dialogFragment.dismiss()
                if (showReasonOrGoSettings) {
                    chainTask.requestAgain(permissions)
                } else {
                    forwardToSettings(permissions)
                }
            }
            if (negativeButton != null) {
                negativeButton.isClickable = true
                negativeButton.setOnClickListener {
                    dialogFragment.dismiss()
                    chainTask.finish()
                }
            }
        }

        /**
         * 在Fragment中立即请求权限
         *
         * @param permissions 要请求的权限
         * @param chainTask   当前任务的实例
         */
        fun requestNow(permissions: Set<String?>?, chainTask: ChainTask?) {
            getInvisibleFragment().requestNow(this, permissions, chainTask)
        }

        /**
         * 在Fragment中立即 ACCESS_BACKGROUND_LOCATION 权限
         *
         * @param chainTask 当前任务的实例
         */
        fun requestAccessBackgroundLocationNow(chainTask: ChainTask?) {
            getInvisibleFragment().requestAccessBackgroundLocationNow(this, chainTask)
        }

        /**
         * Request SYSTEM_ALERT_WINDOW permission at once in the fragment.
         *
         * @param chainTask Instance of current task.
         */
        fun requestSystemAlertWindowPermissionNow(chainTask: ChainTask?) {
            getInvisibleFragment().requestSystemAlertWindowPermissionNow(this, chainTask)
        }

        /**
         * Request WRITE_SETTINGS permission at once in the fragment.
         *
         * @param chainTask Instance of current task.
         */
        fun requestWriteSettingsPermissionNow(chainTask: ChainTask?) {
            getInvisibleFragment().requestWriteSettingsPermissionNow(this, chainTask)
        }

        /**
         * Request MANAGE_EXTERNAL_STORAGE permission at once in the fragment.
         *
         * @param chainTask Instance of current task.
         */
        fun requestManageExternalStoragePermissionNow(chainTask: ChainTask?) {
            getInvisibleFragment().requestManageExternalStoragePermissionNow(this, chainTask)
        }

        /**
         * Should we request ACCESS_BACKGROUND_LOCATION permission or not.
         *
         * @return True if specialPermissions contains ACCESS_BACKGROUND_LOCATION permission, false otherwise.
         */
        fun shouldRequestBackgroundLocationPermission(): Boolean {
            return specialPermissions.contains(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
        }

        /**
         * Should we request SYSTEM_ALERT_WINDOW permission or not.
         *
         * @return True if specialPermissions contains SYSTEM_ALERT_WINDOW permission, false otherwise.
         */
        fun shouldRequestSystemAlertWindowPermission(): Boolean {
            return specialPermissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)
        }

        /**
         * Should we request WRITE_SETTINGS permission or not.
         *
         * @return True if specialPermissions contains WRITE_SETTINGS permission, false otherwise.
         */
        fun shouldRequestWriteSettingsPermission(): Boolean {
            return specialPermissions.contains(Manifest.permission.WRITE_SETTINGS)
        }

        /**
         * Should we request MANAGE_EXTERNAL_STORAGE permission or not.
         *
         * @return True if specialPermissions contains MANAGE_EXTERNAL_STORAGE permission, false otherwise.
         */
        fun shouldRequestManageExternalStoragePermission(): Boolean {
            return specialPermissions.contains(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE)
        }

        /**
         * Get the targetSdkVersion of current app.
         *
         * @return The targetSdkVersion of current app.
         */
        fun getTargetSdkVersion(): Int {
            return activity!!.applicationInfo.targetSdkVersion
        }

        /**
         * Get the FragmentManager if it's in Activity, or the ChildFragmentManager if it's in Fragment.
         * @return The FragmentManager to operate Fragment.
         */
        fun getFragmentManager(): FragmentManager? {
            val fragmentManager: FragmentManager
            fragmentManager = fragment?.childFragmentManager ?: activity!!.supportFragmentManager
            return fragmentManager
        }

        /**
         * Get the invisible fragment in activity for request permissions.
         * If there is no invisible fragment, add one into activity.
         * Don't worry. This is very lightweight.
         */
        private fun getInvisibleFragment(): InvisibleFragment? {
            val fragmentManager: FragmentManager = getFragmentManager()
            val existedFragment = fragmentManager.findFragmentByTag(PermissionBuilder.FRAGMENT_TAG)
            return if (existedFragment != null) {
                existedFragment as InvisibleFragment?
            } else {
                val invisibleFragment = InvisibleFragment()
                fragmentManager.beginTransaction()
                    .add(invisibleFragment, PermissionBuilder.FRAGMENT_TAG)
                    .commitNowAllowingStateLoss()
                invisibleFragment
            }
        }

        /**
         * Go to your app's Settings page to let user turn on the necessary permissions.
         *
         * @param permissions Permissions which are necessary.
         */
        private fun forwardToSettings(permissions: List<String>) {
            forwardPermissions.clear()
            forwardPermissions.addAll(permissions)
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri = Uri.fromParts("package", activity!!.packageName, null)
            intent.data = uri
            getInvisibleFragment().startActivityForResult(intent,
                InvisibleFragment.FORWARD_TO_SETTINGS)
        }
    }
}