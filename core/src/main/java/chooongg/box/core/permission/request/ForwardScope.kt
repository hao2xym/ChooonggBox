package chooongg.box.core.permission.request

import chooongg.box.core.permission.BoxPermission

class ForwardScope(
    private val pb: BoxPermission.PermissionBuilder,
    private val chainTask: ChainTask,
) {
}