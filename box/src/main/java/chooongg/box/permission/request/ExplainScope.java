package chooongg.box.permission.request;

import androidx.annotation.NonNull;

import com.permissionx.guolindev.dialog.RationaleDialog;
import com.permissionx.guolindev.dialog.RationaleDialogFragment;

import java.util.List;

public class ExplainScope {

    private PermissionBuilder pb;

    private ChainTask chainTask;

    ExplainScope(PermissionBuilder pb, ChainTask chainTask) {
        this.pb = pb;
        this.chainTask = chainTask;
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param permissions
     *          Permissions that to request.
     * @param message
     *          Message that show to user.
     * @param positiveText
     *          Text on the positive button. When user click, PermissionX will request permissions again.
     * @param negativeText
     *          Text on the negative button. When user click, PermissionX will finish request.
     */
    public void showRequestReasonDialog(List<String> permissions, String message, String positiveText, String negativeText) {
        pb.showHandlePermissionDialog(chainTask, true, permissions, message, positiveText, negativeText);
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param permissions
     *          Permissions that to request.
     * @param message
     *          Message that show to user.
     * @param positiveText
     *          Text on the positive button. When user click, PermissionX will request permissions again.
     */
    public void showRequestReasonDialog(List<String> permissions, String message, String positiveText) {
        showRequestReasonDialog(permissions, message, positiveText, null);
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param dialog
     *          Dialog to explain to user why these permissions are necessary.
     */
    public void showRequestReasonDialog(@NonNull RationaleDialog dialog) {
        pb.showHandlePermissionDialog(chainTask, true, dialog);
    }

    /**
     * Show a rationale dialog to explain to user why you need these permissions.
     * @param dialogFragment
     *          DialogFragment to explain to user why these permissions are necessary.
     */
    public void showRequestReasonDialog(@NonNull RationaleDialogFragment dialogFragment) {
        pb.showHandlePermissionDialog(chainTask, true, dialogFragment);
    }

}