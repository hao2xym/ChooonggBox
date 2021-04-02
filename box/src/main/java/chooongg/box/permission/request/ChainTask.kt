package chooongg.box.permission.request

/**
 * 定义任务界面以请求权限
 * 并非一次可以请求所有权限
 * 一些权限需要单独请求
 * 因此，每个权限请求都需要实现此接口，并在其实现中执行请求逻辑
 */
interface ChainTask {
    /**
     * Get the ExplainScope for showing RequestReasonDialog.
     *
     * @return Instance of ExplainScope.
     */
    fun getExplainScope(): ExplainScope?

    /**
     * Get the ForwardScope for showing ForwardToSettingsDialog.
     *
     * @return Instance of ForwardScope.
     */
    fun getForwardScope(): ForwardScope?

    /**
     * Do the request logic.
     */
    fun request()

    /**
     * Request permissions again when user denied.
     *
     * @param permissions Permissions to request again.
     */
    fun requestAgain(permissions: List<String?>?)

    /**
     * Finish this task and notify the request result.
     */
    fun finish()
}