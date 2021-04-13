package chooongg.box.core.permission.request

import android.Manifest
import android.annotation.TargetApi
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.provider.Settings
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import chooongg.box.core.permission.BoxPermission
import java.util.*

class InvisibleFragment : Fragment() {

    companion object {
        /**
         * 请求正常的权限
         */
        const val REQUEST_NORMAL_PERMISSIONS = 1

        /**
         * 请求 ACCESS_BACKGROUND_LOCATION 权限
         * 在 Android R 中无法与其他权限一同请求
         */
        const val REQUEST_BACKGROUND_LOCATION_PERMISSION = 2

        /**
         * 跳转到当前应用程序的设置页面
         */
        const val FORWARD_TO_SETTINGS = 1

        /**
         * 请求 SYSTEM_ALERT_WINDOW 权限的行为
         */
        const val ACTION_MANAGE_OVERLAY_PERMISSION = 2

        /**
         * 请求 WRITE_SETTINGS 权限的行为
         */
        const val ACTION_WRITE_SETTINGS_PERMISSION = 3

        /**
         * 请求 MANAGE_EXTERNAL_STORAGE 权限的行为
         */
        const val ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION = 4
    }

    private lateinit var pb: BoxPermission.PermissionBuilder

    /**
     * 当前任务的实例
     */
    private lateinit var task: ChainTask

    /**
     * 通过调用[Fragment.requestPermissions]立即请求权限
     * 并在 ActivityCompat.OnRequestPermissionsResultCallback 中处理请求结果
     *
     * @param permissionBuilder PermissionBuilder的实例
     * @param permissions       要请求的权限
     * @param chainTask         当前任务的实例
     */
    fun requestNow(
        permissionBuilder: BoxPermission.PermissionBuilder,
        permissions: Set<String>,
        chainTask: ChainTask,
    ) {
        pb = permissionBuilder
        task = chainTask
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            onRequestNormalPermissionsResult(it)
        }.launch(permissions.toTypedArray())
    }

    /**
     * 通过调用[Fragment.requestPermissions]立即请求 ACCESS_BACKGROUND_LOCATION
     * 并在 ActivityCompat.OnRequestPermissionsResultCallback 中处理请求结果
     *
     * @param permissionBuilder PermissionBuilder的实例
     * @param chainTask         当前任务的实例
     */
    fun requestAccessBackgroundLocationNow(
        permissionBuilder: BoxPermission.PermissionBuilder,
        chainTask: ChainTask,
    ) {
        pb = permissionBuilder
        task = chainTask
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            onRequestBackgroundLocationPermissionResult()
        }.launch(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
    }

    /**
     * 申请 SYSTEM_ALERT_WINDOW 权限
     * 在 Android M 及更高版本上
     * 它是由具有 Settings.ACTION_MANAGE_OVERLAY_PERMISSION 的 Intent 请求的
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun requestSystemAlertWindowPermissionNow(
        permissionBuilder: BoxPermission.PermissionBuilder,
        chainTask: ChainTask,
    ) {
        pb = permissionBuilder
        task = chainTask
        if (!Settings.canDrawOverlays(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
            startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION)
        } else {
            onRequestSystemAlertWindowPermissionResult()
        }
    }

    /**
     * 申请 WRITE_SETTINGS 权限
     * 在 Android M 及更高版本上
     * 它由具有 Settings.ACTION_MANAGE_WRITE_SETTINGS 的 Intent 请求
     */
    @TargetApi(Build.VERSION_CODES.M)
    fun requestWriteSettingsPermissionNow(
        permissionBuilder: BoxPermission.PermissionBuilder,
        chainTask: ChainTask,
    ) {
        pb = permissionBuilder
        task = chainTask
        if (!Settings.System.canWrite(context)) {
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            startActivityForResult(intent, ACTION_WRITE_SETTINGS_PERMISSION)
        } else {
            onRequestWriteSettingsPermissionResult()
        }
    }

    /**
     * 申请 MANAGE_EXTERNAL_STORAGE 权限
     * 在 Android R 及更高版本上
     * 它是由具有 Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION 的 Intent 请求的
     */
    fun requestManageExternalStoragePermissionNow(
        permissionBuilder: BoxPermission.PermissionBuilder,
        chainTask: ChainTask,
    ) {
        pb = permissionBuilder
        task = chainTask
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && !Environment.isExternalStorageManager()) {
            val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
            startActivityForResult(intent, ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        } else {
            onRequestManageExternalStoragePermissionResult()
        }
    }

    /**
     * 当用户从“设置”切换回时，处理请求结果
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // When user switch back from settings, just request again.
        if (checkForGC()) {
            when (requestCode) {
                FORWARD_TO_SETTINGS -> task.requestAgain(ArrayList(pb.forwardPermissions))
                ACTION_MANAGE_OVERLAY_PERMISSION -> onRequestSystemAlertWindowPermissionResult()
                ACTION_WRITE_SETTINGS_PERMISSION -> onRequestWriteSettingsPermissionResult()
                ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION -> onRequestManageExternalStoragePermissionResult()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (checkForGC()) {
            // Dismiss the showing dialog when InvisibleFragment destroyed for avoiding window leak problem.
            if (pb.currentDialog != null && pb.currentDialog!!.isShowing) {
                pb.currentDialog!!.dismiss()
            }
        }
    }

    /**
     * Handle result of normal permissions request.
     */
    private fun onRequestNormalPermissionsResult(
        permissions: Array<String>?,
        grantResults: IntArray?,
    ) {
        if (checkForGC() && permissions != null && grantResults != null && permissions.size == grantResults.size) {
            // We can never holds granted permissions for safety, because user may turn some permissions off in settings.
            // So every time request, must request the already granted permissions again and refresh the granted permission set.
            pb.grantedPermissions.clear()
            val showReasonList: MutableList<String> =
                ArrayList() // holds denied permissions in the request permissions.
            val forwardList: MutableList<String> =
                ArrayList() // hold permanently denied permissions in the request permissions.
            for (i in permissions.indices) {
                val permission = permissions[i]
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    pb.grantedPermissions.add(permission)
                    // Remove granted permissions from deniedPermissions and permanentDeniedPermissions set in PermissionBuilder.
                    pb.deniedPermissions.remove(permission)
                    pb.permanentDeniedPermissions.remove(permission)
                } else {
                    // Denied permission can turn into permanent denied permissions, but permanent denied permission can not turn into denied permissions.
                    val shouldShowRationale = shouldShowRequestPermissionRationale(permission)
                    if (shouldShowRationale) {
                        showReasonList.add(permissions[i])
                        pb.deniedPermissions.add(permission)
                        // So there's no need to remove the current permission from permanentDeniedPermissions because it won't be there.
                    } else {
                        forwardList.add(permissions[i])
                        pb.permanentDeniedPermissions.add(permission)
                        // We must remove the current permission from deniedPermissions because it is permanent denied permission now.
                        pb.deniedPermissions.remove(permission)
                    }
                }
            }
            val deniedPermissions: MutableList<String> =
                ArrayList() // used to validate the deniedPermissions and permanentDeniedPermissions
            deniedPermissions.addAll(pb!!.deniedPermissions)
            deniedPermissions.addAll(pb!!.permanentDeniedPermissions)
            // maybe user can turn some permissions on in settings that we didn't request, so check the denied permissions again for safety.
            for (permission in deniedPermissions) {
                if (BoxPermission.isGranted(context, permission)) {
                    pb!!.deniedPermissions.remove(permission)
                    pb!!.grantedPermissions.add(permission)
                }
            }
            val allGranted = pb!!.grantedPermissions.size() === pb.normalPermissions.size()
            if (allGranted) { // If all permissions are granted, finish current task directly.
                task!!.finish()
            } else {
                var shouldFinishTheTask = true // Indicate if we should finish the task
                // If explainReasonCallback is not null and there're denied permissions. Try the ExplainReasonCallback.
                if ((pb!!.explainReasonCallback != null || pb!!.explainReasonCallbackWithBeforeParam != null) && !showReasonList.isEmpty()) {
                    shouldFinishTheTask =
                        false // shouldn't because ExplainReasonCallback handles it
                    if (pb!!.explainReasonCallbackWithBeforeParam != null) {
                        // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                        pb!!.explainReasonCallbackWithBeforeParam!!.onExplainReason(task!!.getExplainScope(),
                            ArrayList<E>(
                                pb!!.deniedPermissions),
                            false)
                    } else {
                        pb!!.explainReasonCallback!!.onExplainReason(task!!.getExplainScope(),
                            ArrayList<E>(
                                pb!!.deniedPermissions))
                    }
                    // store these permanently denied permissions or they will be lost when request again.
                    pb!!.tempPermanentDeniedPermissions.addAll(forwardList)
                } else if (pb!!.forwardToSettingsCallback != null && (!forwardList.isEmpty() || !pb!!.tempPermanentDeniedPermissions.isEmpty())) {
                    shouldFinishTheTask =
                        false // shouldn't because ForwardToSettingsCallback handles it
                    pb!!.tempPermanentDeniedPermissions.clear() // no need to store them anymore once onForwardToSettings callback.
                    pb!!.forwardToSettingsCallback!!.onForwardToSettings(task!!.getForwardScope(),
                        ArrayList<E>(
                            pb!!.permanentDeniedPermissions))
                }
                // If showRequestReasonDialog or showForwardToSettingsDialog is not called. We should finish the task.
                // There's case that ExplainReasonCallback or ForwardToSettingsCallback is called, but developer didn't invoke
                // showRequestReasonDialog or showForwardToSettingsDialog in the callback.
                // At this case and all other cases, task should be finished.
                if (shouldFinishTheTask || !pb!!.showDialogCalled) {
                    task!!.finish()
                }
                // Reset this value after each request. If we don't do this, developer invoke showRequestReasonDialog in ExplainReasonCallback
                // but didn't invoke showForwardToSettingsDialog in ForwardToSettingsCallback, the request process will be lost. Because the
                // previous showDialogCalled affect the next request logic.
                pb!!.showDialogCalled = false
            }
        }
    }

    /**
     * Handle result of normal permissions request.
     */
    private fun onRequestNormalPermissionsResult(permissions: Map<String, Boolean>?) {
        if (checkForGC() && permissions != null) {
            // We can never holds granted permissions for safety, because user may turn some permissions off in settings.
            // So every time request, must request the already granted permissions again and refresh the granted permission set.
            pb.grantedPermissions.clear()
            val showReasonList: MutableList<String> =
                ArrayList() // holds denied permissions in the request permissions.
            val forwardList: MutableList<String> =
                ArrayList() // hold permanently denied permissions in the request permissions.
            permissions.forEach {
                if (it.value) {
                    pb.grantedPermissions.add(it.key)
                    // 从PermissionBuilder中设置的denizedPermissions和permanentDeniedPermissions中删除授予的权限
                    pb.deniedPermissions.remove(it.key)
                    pb.permanentDeniedPermissions.remove(it.key)
                } else {
                    if (shouldShowRequestPermissionRationale(it.key)) {
                        showReasonList.add(it.key)
                        pb.deniedPermissions.add(it.key)
                    } else {

                    }
                }
            }
            for (i in permissions.entries) {
                val permission = permissions
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    pb!!.grantedPermissions.add(permission)
                    // Remove granted permissions from deniedPermissions and permanentDeniedPermissions set in PermissionBuilder.
                    pb!!.deniedPermissions.remove(permission)
                    pb!!.permanentDeniedPermissions.remove(permission)
                } else {
                    // Denied permission can turn into permanent denied permissions, but permanent denied permission can not turn into denied permissions.
                    val shouldShowRationale = shouldShowRequestPermissionRationale(permission)
                    if (shouldShowRationale) {
                        showReasonList.add(permissions[i])
                        pb!!.deniedPermissions.add(permission)
                        // So there's no need to remove the current permission from permanentDeniedPermissions because it won't be there.
                    } else {
                        forwardList.add(permissions[i])
                        pb!!.permanentDeniedPermissions.add(permission)
                        // We must remove the current permission from deniedPermissions because it is permanent denied permission now.
                        pb!!.deniedPermissions.remove(permission)
                    }
                }
            }
            val deniedPermissions: MutableList<String> =
                ArrayList() // used to validate the deniedPermissions and permanentDeniedPermissions
            deniedPermissions.addAll(pb!!.deniedPermissions)
            deniedPermissions.addAll(pb!!.permanentDeniedPermissions)
            // maybe user can turn some permissions on in settings that we didn't request, so check the denied permissions again for safety.
            for (permission in deniedPermissions) {
                if (BoxPermission.isGranted(context, permission)) {
                    pb!!.deniedPermissions.remove(permission)
                    pb!!.grantedPermissions.add(permission)
                }
            }
            val allGranted = pb!!.grantedPermissions.size() === pb.normalPermissions.size()
            if (allGranted) { // If all permissions are granted, finish current task directly.
                task!!.finish()
            } else {
                var shouldFinishTheTask = true // Indicate if we should finish the task
                // If explainReasonCallback is not null and there're denied permissions. Try the ExplainReasonCallback.
                if ((pb!!.explainReasonCallback != null || pb!!.explainReasonCallbackWithBeforeParam != null) && !showReasonList.isEmpty()) {
                    shouldFinishTheTask =
                        false // shouldn't because ExplainReasonCallback handles it
                    if (pb!!.explainReasonCallbackWithBeforeParam != null) {
                        // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                        pb!!.explainReasonCallbackWithBeforeParam!!.onExplainReason(task!!.getExplainScope(),
                            ArrayList<E>(
                                pb!!.deniedPermissions),
                            false)
                    } else {
                        pb!!.explainReasonCallback!!.onExplainReason(task!!.getExplainScope(),
                            ArrayList<E>(
                                pb!!.deniedPermissions))
                    }
                    // store these permanently denied permissions or they will be lost when request again.
                    pb!!.tempPermanentDeniedPermissions.addAll(forwardList)
                } else if (pb!!.forwardToSettingsCallback != null && (!forwardList.isEmpty() || !pb!!.tempPermanentDeniedPermissions.isEmpty())) {
                    shouldFinishTheTask =
                        false // shouldn't because ForwardToSettingsCallback handles it
                    pb!!.tempPermanentDeniedPermissions.clear() // no need to store them anymore once onForwardToSettings callback.
                    pb!!.forwardToSettingsCallback!!.onForwardToSettings(task!!.getForwardScope(),
                        ArrayList<E>(
                            pb!!.permanentDeniedPermissions))
                }
                // If showRequestReasonDialog or showForwardToSettingsDialog is not called. We should finish the task.
                // There's case that ExplainReasonCallback or ForwardToSettingsCallback is called, but developer didn't invoke
                // showRequestReasonDialog or showForwardToSettingsDialog in the callback.
                // At this case and all other cases, task should be finished.
                if (shouldFinishTheTask || !pb!!.showDialogCalled) {
                    task!!.finish()
                }
                // Reset this value after each request. If we don't do this, developer invoke showRequestReasonDialog in ExplainReasonCallback
                // but didn't invoke showForwardToSettingsDialog in ForwardToSettingsCallback, the request process will be lost. Because the
                // previous showDialogCalled affect the next request logic.
                pb!!.showDialogCalled = false
            }
        }
    }

    /**
     * Handle result of ACCESS_BACKGROUND_LOCATION permission request.
     */
    private fun onRequestBackgroundLocationPermissionResult() {
        if (checkForGC()) {
            if (PermissionX.isGranted(context,
                    RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
            ) {
                pb!!.grantedPermissions.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                // Remove granted permissions from deniedPermissions and permanentDeniedPermissions set in PermissionBuilder.
                pb!!.deniedPermissions.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                pb!!.permanentDeniedPermissions.remove(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                task!!.finish()
            } else {
                var goesToRequestCallback = true // Indicate if we should finish the task
                val shouldShowRationale =
                    shouldShowRequestPermissionRationale(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                // If explainReasonCallback is not null and we should show rationale. Try the ExplainReasonCallback.
                if ((pb!!.explainReasonCallback != null || pb!!.explainReasonCallbackWithBeforeParam != null) && shouldShowRationale) {
                    goesToRequestCallback =
                        false // shouldn't because ExplainReasonCallback handles it
                    val permissionsToExplain: MutableList<String?> = ArrayList()
                    permissionsToExplain.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    if (pb!!.explainReasonCallbackWithBeforeParam != null) {
                        // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                        pb!!.explainReasonCallbackWithBeforeParam!!.onExplainReason(task!!.getExplainScope(),
                            permissionsToExplain,
                            false)
                    } else {
                        pb!!.explainReasonCallback!!.onExplainReason(task!!.getExplainScope(),
                            permissionsToExplain)
                    }
                } else if (pb!!.forwardToSettingsCallback != null && !shouldShowRationale) {
                    goesToRequestCallback =
                        false // shouldn't because ForwardToSettingsCallback handles it
                    val permissionsToForward: MutableList<String?> = ArrayList()
                    permissionsToForward.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                    pb!!.forwardToSettingsCallback!!.onForwardToSettings(task!!.getForwardScope(),
                        permissionsToForward)
                }
                // If showRequestReasonDialog or showForwardToSettingsDialog is not called. We should finish the task.
                // There's case that ExplainReasonCallback or ForwardToSettingsCallback is called, but developer didn't invoke
                // showRequestReasonDialog or showForwardToSettingsDialog in the callback.
                // At this case and all other cases, task should be finished.
                if (goesToRequestCallback || !pb!!.showDialogCalled) {
                    task!!.finish()
                }
            }
        }
    }

    /**
     * Handle result of SYSTEM_ALERT_WINDOW permission request.
     */
    private fun onRequestSystemAlertWindowPermissionResult() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(context)) {
                task!!.finish()
            } else if (pb!!.explainReasonCallback != null || pb!!.explainReasonCallbackWithBeforeParam != null) {
                if (pb!!.explainReasonCallbackWithBeforeParam != null) {
                    // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                    pb!!.explainReasonCallbackWithBeforeParam!!.onExplainReason(task!!.getExplainScope(),
                        listOf(
                            Manifest.permission.SYSTEM_ALERT_WINDOW),
                        false)
                } else {
                    pb!!.explainReasonCallback!!.onExplainReason(task!!.getExplainScope(), listOf(
                        Manifest.permission.SYSTEM_ALERT_WINDOW))
                }
            }
        } else {
            task!!.finish()
        }
    }

    /**
     * Handle result of WRITE_SETTINGS permission request.
     */
    private fun onRequestWriteSettingsPermissionResult() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.System.canWrite(context)) {
                task!!.finish()
            } else if (pb!!.explainReasonCallback != null || pb!!.explainReasonCallbackWithBeforeParam != null) {
                if (pb!!.explainReasonCallbackWithBeforeParam != null) {
                    // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                    pb!!.explainReasonCallbackWithBeforeParam!!.onExplainReason(task!!.getExplainScope(),
                        listOf(
                            Manifest.permission.WRITE_SETTINGS),
                        false)
                } else {
                    pb!!.explainReasonCallback!!.onExplainReason(task!!.getExplainScope(), listOf(
                        Manifest.permission.WRITE_SETTINGS))
                }
            }
        } else {
            task!!.finish()
        }
    }

    /**
     * Handle result of MANAGE_EXTERNAL_STORAGE permission request.
     */
    private fun onRequestManageExternalStoragePermissionResult() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                task!!.finish()
            } else if (pb!!.explainReasonCallback != null || pb!!.explainReasonCallbackWithBeforeParam != null) {
                if (pb!!.explainReasonCallbackWithBeforeParam != null) {
                    // callback ExplainReasonCallbackWithBeforeParam prior to ExplainReasonCallback
                    pb!!.explainReasonCallbackWithBeforeParam!!.onExplainReason(task!!.getExplainScope(),
                        listOf(
                            Manifest.permission.MANAGE_EXTERNAL_STORAGE),
                        false)
                } else {
                    pb!!.explainReasonCallback!!.onExplainReason(task!!.getExplainScope(), listOf(
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE))
                }
            }
        } else {
            task!!.finish()
        }
    }

    /**
     * On some phones, PermissionBuilder and ChainTask may become null under unpredictable occasions such as GC.
     * They should not be null at this time, so we can do nothing in this case.
     * @return PermissionBuilder and ChainTask are still alive or not. If not, we should not do any further logic.
     */
    private fun checkForGC(): Boolean {
        if (pb == null || task == null) {
            Log.w(
                "PermissionX",
                "PermissionBuilder and ChainTask should not be null at this time, so we can do nothing in this case."
            )
            return false
        }
        return true
    }
}