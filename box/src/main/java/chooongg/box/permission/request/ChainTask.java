package chooongg.box.permission.request;

import java.util.List;

/**
 * 定义任务界面以请求权限
 * 并非一次可以请求所有权限
 * 一些权限需要单独请求
 * 因此，每个权限请求都需要实现此接口，并在其实现中执行请求逻辑
 */
public interface ChainTask {

    /**
     * Get the ExplainScope for showing RequestReasonDialog.
     *
     * @return Instance of ExplainScope.
     */
    ExplainScope getExplainScope();

    /**
     * Get the ForwardScope for showing ForwardToSettingsDialog.
     *
     * @return Instance of ForwardScope.
     */
    ForwardScope getForwardScope();

    /**
     * Do the request logic.
     */
    void request();

    /**
     * Request permissions again when user denied.
     *
     * @param permissions Permissions to request again.
     */
    void requestAgain(List<String> permissions);

    /**
     * Finish this task and notify the request result.
     */
    void finish();
}