package chooongg.box.permission.request

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

class PermissionBuilder(
    val activity: FragmentActivity?,
    val fragment: Fragment?,
    val normalPermissions: Set<String>,
    val specialPermissions: Set<String>
) {

    var explainReasonCallback: ((scope: ExplainScope, deniedList: List<String>) -> Unit)? = null

    var explainReasonCallbackWithBeforeParam: ((scope: ExplainScope, deniedList: List<String>, beforeRequest: Boolean) -> Unit)? =
        null

    var forwardToSettingsCallback: ((scope: ForwardScope, deniedList: List<String>) -> Unit)? = null

    var explainReasonBeforeRequest = false

    var requestCallback: ((allGranted: Boolean, grantedList: List<String>, deniedList: List<String>) -> Unit)? =
        null

    fun onExplainRequestReason(callback: (scope: ExplainScope, deniedList: List<String>) -> Unit) =
        apply {
            explainReasonCallback = callback
        }

    fun onExplainRequestReason(callback: (scope: ExplainScope, deniedList: List<String>, beforeRequest: Boolean) -> Unit) =
        apply {
            explainReasonCallbackWithBeforeParam = callback
        }

    fun onForwardToSettings(callback: (scope: ForwardScope, deniedList: List<String>) -> Unit) =
        apply {
            forwardToSettingsCallback = callback
        }

    fun explainReasonBeforeRequest() = apply { explainReasonBeforeRequest = true }

    fun request(callback: (allGranted: Boolean, grantedList: List<String>, deniedList: List<String>) -> Unit) {
        requestCallback = callback
        val requestChain = RequestChain()
        requestChain.addTaskToChain(RequestNormalPermissions(this))
        requestChain.addTaskToChain(RequestBackgroundLocationPermission(this))
        requestChain.addTaskToChain(RequestSystemAlertWindowPermission(this))
        requestChain.addTaskToChain(RequestWriteSettingsPermission(this))
        requestChain.addTaskToChain(RequestManageExternalStoragePermission(this))
        requestChain.runTask()
    }
}