package chooongg.box.core.permission.request

interface ChainTask {
    /**
     * 获取用于显示请求原因弹框的ExplainScope。
     */
    fun getExplainScope(): ExplainScope

    /**
     * 获取用于显示跳转权限设置弹框的ForwardScope。
     * @return Instance of ForwardScope.
     */
    fun getForwardScope(): ForwardScope

    /**
     * 执行请求逻辑
     */
    fun request()

    /**
     * 用户被拒绝时，再次请求权限
     */
    fun requestAgain(permissions: List<String?>?)

    /**
     * 完成此任务并通知请求结果
     */
    fun finish()
}