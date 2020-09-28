package chooongg.box.ext

import android.widget.Toast
import androidx.annotation.DrawableRes
import chooongg.box.utils.R
import chooongg.box.widget.ChooonggToast

private var chooonggToast: Toast? = null

private var defaultType = ChooonggToast.TYPE_EMPHASIZE

fun toastCancel() {
    chooonggToast?.cancel()
    chooonggToast = null
}

fun toastSuccess(
    message: CharSequence?,
    duration: Int = -1,
    @ChooonggToast.Type type: Int = defaultType
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
    ).show { chooonggToast = it }
}

fun toastWarn(
    message: CharSequence?,
    duration: Int = -1,
    @ChooonggToast.Type type: Int = defaultType
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
    ).show { chooonggToast = it }
}

fun toastError(
    message: CharSequence?,
    duration: Int = -1,
    @ChooonggToast.Type type: Int = defaultType
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
    ).show { chooonggToast = it }
}

fun toast(
    message: CharSequence?,
    @DrawableRes icon: Int? = null
) {
    ChooonggToast.make(message, icon, ChooonggToast.TYPE_UNIVERSAL, Toast.LENGTH_SHORT).show { chooonggToast = it }
}

fun toastEmphasize(
    message: CharSequence?,
    @DrawableRes icon: Int? = null
) {
    ChooonggToast.make(message, icon, ChooonggToast.TYPE_EMPHASIZE, Toast.LENGTH_LONG).show { chooonggToast = it }
}