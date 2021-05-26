package chooongg.box.core.permission.request

import chooongg.box.core.permission.BoxPermission
import java.util.*

/**
 * 实现请求普通权限
 */
class RequestNormalPermissions(permissionBuilder: BoxPermission.PermissionBuilder) :
    BaseTask(permissionBuilder) {
    override fun request() {
        val requestList: MutableList<String> = ArrayList()
        for (permission in pb.normalPermissions) {
            if (BoxPermission.isGranted(pb.activity, permission)) {
                pb.grantedPermissions.add(permission) // 已经授予
            } else {
                requestList.add(permission) // 仍然需要授予
            }
        }
        if (requestList.isEmpty()) { // 所有权限已经授予
            finish()
            return
        }
        if (pb.explainReasonBeforeRequest && (pb.explainReasonCallback != null || pb.explainReasonCallbackWithBeforeParam != null)) {
            pb.explainReasonBeforeRequest = false
            pb.deniedPermissions.addAll(requestList)
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
            // 立即执行请求。始终请求所有权限，无论是否已授予它们，以防用户在“设置”中将其关闭
            pb.requestNow(pb.normalPermissions, this)
        }
    }

    /**
     * 如果用户拒绝许可，并且 {@link ExplainScope#showRequestReasonDialog(List, String, String)} 或者
     * {@link ForwardScope#showForwardToSettingsDialog(List, String, String)} 被调用
     * 则当用户单击肯定按钮时，将调用此方法
     * @param permissions 再次请求的权限
     */
    override fun requestAgain(permissions: List<String>?) {
        val permissionsToRequestAgain: MutableSet<String> = HashSet(pb.grantedPermissions)
        permissionsToRequestAgain.addAll(permissions!!)
        pb.requestNow(permissionsToRequestAgain, this)
    }
}