package chooongg.box.core.permission.request

import android.Manifest
import android.os.Build
import chooongg.box.core.permission.BoxPermission
import java.util.*

class RequestBackgroundLocationPermission(permissionBuilder: BoxPermission.PermissionBuilder) :
    BaseTask(permissionBuilder) {
    companion object {
        /**
         * 最低兼容系统版本 Q
         */
        const val ACCESS_BACKGROUND_LOCATION = "android.permission.ACCESS_BACKGROUND_LOCATION"
    }

    override fun request() {
        if (pb.shouldRequestBackgroundLocationPermission()) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                // If app runs under Android Q, there's no ACCESS_BACKGROUND_LOCATION permissions.
                // We remove it from request list, but will append it to the request callback as denied permission.
                pb.specialPermissions.remove(ACCESS_BACKGROUND_LOCATION)
                pb.permissionsWontRequest.add(ACCESS_BACKGROUND_LOCATION)
            }
            if (BoxPermission.isGranted(pb.activity, ACCESS_BACKGROUND_LOCATION)) {
                // ACCESS_BACKGROUND_LOCATION has already granted, we can finish this task now.
                finish()
                return
            }
            val accessFindLocationGranted: Boolean = BoxPermission.isGranted(
                pb.activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            val accessCoarseLocationGranted: Boolean = BoxPermission.isGranted(
                pb.activity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            if (accessFindLocationGranted || accessCoarseLocationGranted) {
                if (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null) {
                    val requestList: MutableList<String?> = ArrayList()
                    requestList.add(ACCESS_BACKGROUND_LOCATION)
                    if (pb.explainReasonCallbackWithBeforeParam != null) {
                        // 在 ExplainReasonCallback 之前回调 ExplainReasonCallbackWithBeforeParam
                        pb.explainReasonCallbackWithBeforeParam!!.onExplainReason(
                            explainReasonScope,
                            requestList,
                            true
                        )
                    } else {
                        pb.explainReasonCallback!!.onExplainReason(explainReasonScope, requestList)
                    }
                } else {
                    // 没有实现 explainReasonCallback 因此我们必须请求 ACCESS_BACKGROUND_LOCATION 而无需解释。
                    requestAgain(null)
                }
                return
            }
        }
        // 目前不应该请求 ACCESS_BACKGROUND_LOCATION 因此我们调用finish()完成此任务
        finish()
    }

    override fun requestAgain(permissions: List<String>?) {
        // 不在乎权限参数是什么，始终请求 ACCESS_BACKGROUND_LOCATION
        pb.requestAccessBackgroundLocationNow(this)
    }

}