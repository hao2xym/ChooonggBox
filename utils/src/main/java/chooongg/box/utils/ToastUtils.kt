package chooongg.box.utils

import android.widget.Toast
import androidx.annotation.DrawableRes
import chooongg.box.widget.ChooonggToast

object ToastUtils {

    var chooonggToast:Toast? = null

    fun cancel(){
        chooonggToast?.cancel()
        chooonggToast = null
    }

    fun showSuccess(
        message: CharSequence?,
        duration: Int = -1,
        @ChooonggToast.Type type: Int = ChooonggToast.TYPE_EMPHASIZE
    ) {
        ChooonggToast.make(
            message,
            if (type == ChooonggToast.TYPE_EMPHASIZE) {
                R.drawable.ic_toast_success_emphasize
            } else {
                R.drawable.ic_toast_success_universal
            },
            type,
            if (duration == -1) {
                if ((message?.length ?: 0) > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            } else duration
        ).show()
    }

    fun showWarn(
        message: CharSequence?,
        duration: Int = -1,
        @ChooonggToast.Type type: Int = ChooonggToast.TYPE_EMPHASIZE
    ) {
        ChooonggToast.make(
            message,
            if (type == ChooonggToast.TYPE_EMPHASIZE) {
                R.drawable.ic_toast_warn_emphasize
            } else {
                R.drawable.ic_toast_warn_universal
            },
            type,
            if (duration == -1) {
                if ((message?.length ?: 0) > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            } else duration
        ).show()
    }

    fun showError(
        message: CharSequence?,
        duration: Int = -1,
        @ChooonggToast.Type type: Int = ChooonggToast.TYPE_EMPHASIZE
    ) {
        ChooonggToast.make(
            message,
            if (type == ChooonggToast.TYPE_EMPHASIZE) {
                R.drawable.ic_toast_error_emphasize
            } else {
                R.drawable.ic_toast_error_universal
            },
            type,
            if (duration == -1) {
                if ((message?.length ?: 0) > 10) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
            } else duration
        ).show()
    }

    fun showEmphasize(
        message: CharSequence?,
        @DrawableRes icon: Int? = null
    ) {
        ChooonggToast.make(message, icon, ChooonggToast.TYPE_EMPHASIZE, Toast.LENGTH_LONG).show()
    }

    fun show(
        message: CharSequence?,
        @DrawableRes icon: Int? = null
    ) {
        ChooonggToast.make(message, icon, ChooonggToast.TYPE_UNIVERSAL, Toast.LENGTH_SHORT).show()
    }
}