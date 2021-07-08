package chooongg.box.picker

import android.app.Activity
import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment

abstract class FilePickerSelectCreator internal constructor(
    private val filePicker: FilePicker
) {

    init {
        FilePickerSelectOptions.reset()
    }

    fun setTheme(@StyleRes themeId: Int) = apply {
        FilePickerSelectOptions.themeId = themeId
    }

    fun setMaxCount(@IntRange(from = 1) maxCount: Int) = apply {
        FilePickerSelectOptions.maxCount = maxCount
    }

    fun start() {
        onStartActivity(filePicker.getActivity(), filePicker.getFragment())
    }

    abstract fun onStartActivity(activity: Activity?, fragment: Fragment?)

}