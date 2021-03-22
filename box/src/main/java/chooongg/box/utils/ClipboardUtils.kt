package chooongg.box.utils

import android.content.ClipData
import android.content.ClipboardManager
import chooongg.box.ext.APP
import chooongg.box.ext.clipboardManager


object ClipboardUtils {
    /**
     * Copy the text to clipboard.
     * The label equals name of package.
     * @param text The text.
     */
    fun copyText(text: CharSequence?) {
        APP.clipboardManager.setPrimaryClip(ClipData.newPlainText(APP.packageName, text))
    }

    /**
     * Copy the text to clipboard.
     * @param label The label.
     * @param text  The text.
     */
    fun copyText(label: CharSequence?, text: CharSequence?) {
        APP.clipboardManager.setPrimaryClip(ClipData.newPlainText(label, text))
    }

    /**
     * Clear the clipboard.
     */
    fun clear() {
        APP.clipboardManager.setPrimaryClip(ClipData.newPlainText(null, ""))
    }

    /**
     * Return the label for clipboard.
     * @return the label for clipboard
     */
    fun getLabel(): CharSequence {
        return APP.clipboardManager.primaryClipDescription?.label ?: return ""
    }

    /**
     * Return the text for clipboard.
     * @return the text for clipboard
     */
    fun getText(): CharSequence {
        val cm: ClipboardManager = APP.clipboardManager
        val clip: ClipData? = cm.primaryClip
        if (clip != null && clip.itemCount > 0) {
            val text = clip.getItemAt(0).coerceToText(APP)
            if (text != null) return text
        }
        return ""
    }

    /**
     * Add the clipboard changed listener.
     */
    fun addChangedListener(listener: ClipboardManager.OnPrimaryClipChangedListener?) {
        APP.clipboardManager.addPrimaryClipChangedListener(listener)
    }

    /**
     * Remove the clipboard changed listener.
     */
    fun removeChangedListener(listener: ClipboardManager.OnPrimaryClipChangedListener?) {
        APP.clipboardManager.removePrimaryClipChangedListener(listener)
    }
}