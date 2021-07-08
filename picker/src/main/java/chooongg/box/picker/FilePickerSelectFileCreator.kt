package chooongg.box.picker

import android.app.Activity
import androidx.fragment.app.Fragment
import chooongg.box.ext.startActivity
import chooongg.box.picker.activity.FilePickerSelectFileActivity
import chooongg.box.picker.model.FilePickerSortType

class FilePickerSelectFileCreator(
    filePicker: FilePicker
) : FilePickerSelectCreator(filePicker) {

    fun onlyShowCommonly() = apply {
        FilePickerSelectOptions.onlyShowCommonly = true
    }

    fun onlyShowBrowser() = apply {
        FilePickerSelectOptions.onlyShowBrowser = true
    }

    fun setFileTypes(vararg fileType: String) = apply {
        FilePickerSelectOptions.fileTypes = fileType
    }

    fun setSortType(sortType: FilePickerSortType) = apply {
        FilePickerSelectOptions.sortType = sortType
    }

    override fun onStartActivity(activity: Activity?, fragment: Fragment?) {
        fragment?.startActivity(FilePickerSelectFileActivity::class)
            ?: activity?.startActivity(FilePickerSelectFileActivity::class)
    }

}