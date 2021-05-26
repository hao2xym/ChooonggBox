package chooongg.box.core.permission.request

import android.Manifest
import android.os.Build
import android.os.Environment
import android.provider.Settings
import chooongg.box.core.permission.BoxPermission
import java.util.*

abstract class BaseTask internal constructor(internal val pb: BoxPermission.PermissionBuilder) :
    ChainTask {

    /**
     * 指向下一个任务
     * 完成此任务后，将运行下一个任务
     * 如果没有下一个任务，则请求过程结束
     */
    var next: ChainTask? = null

    /**
     * 提供对explainReasonCallback的特定范围，以调用特定的函数。
     */
    val explainReasonScope = ExplainScope(pb, this)

    /**
     * 为要调用的特定函数提供forwardToSettingsCallback的特定范围。
     */
    val forwardToSettingsScope = ForwardScope(pb, this)

    override fun getExplainScope() = explainReasonScope
    override fun getForwardScope() = forwardToSettingsScope

    override fun finish() {
        if (next != null) { // If there's next task, then run it.
            next!!.request()
        } else { // If there's no next task, finish the request process and notify the result
            val deniedList: MutableList<String> = ArrayList()
            deniedList.addAll(pb.deniedPermissions)
            deniedList.addAll(pb.permanentDeniedPermissions)
            deniedList.addAll(pb.permissionsWontRequest)
            if (pb.shouldRequestBackgroundLocationPermission()) {
                if (BoxPermission.isGranted(
                        pb.activity,
                        RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION
                    )
                ) {
                    pb.grantedPermissions.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                } else {
                    deniedList.add(RequestBackgroundLocationPermission.ACCESS_BACKGROUND_LOCATION)
                }
            }
            if (pb.shouldRequestSystemAlertWindowPermission()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.getTargetSdkVersion() >= Build.VERSION_CODES.M
            ) {
                if (Settings.canDrawOverlays(pb.activity)) {
                    pb.grantedPermissions.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
                } else {
                    deniedList.add(Manifest.permission.SYSTEM_ALERT_WINDOW)
                }
            }
            if (pb.shouldRequestWriteSettingsPermission()
                && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && pb.getTargetSdkVersion() >= Build.VERSION_CODES.M
            ) {
                if (Settings.System.canWrite(pb.activity)) {
                    pb.grantedPermissions.add(Manifest.permission.WRITE_SETTINGS)
                } else {
                    deniedList.add(Manifest.permission.WRITE_SETTINGS)
                }
            }
            if (pb.shouldRequestManageExternalStoragePermission()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R &&
                    Environment.isExternalStorageManager()
                ) {
                    pb.grantedPermissions.add(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE)
                } else {
                    deniedList.add(RequestManageExternalStoragePermission.MANAGE_EXTERNAL_STORAGE)
                }
            }
            if (pb.requestCallback != null) {
                pb.requestCallback!!.onResult(
                    deniedList.isEmpty(),
                    ArrayList(pb.grantedPermissions),
                    deniedList
                )
            }
        }
    }

}