package chooongg.box.picker

import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import chooongg.box.ext.startActivity
import chooongg.box.picker.activity.FilePickerSelectFileActivity
import chooongg.box.picker.model.FilePickerSortType
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class FilePickerSelectFileCreator(
    filePicker: FilePicker
) : FilePickerSelectCreator(filePicker) {

    fun setTheme(@StyleRes themeId: Int) = apply {
        FilePickerSelectOptions.themeId = themeId
    }

    fun maxCount(@IntRange(from = 1) maxCount: Int) = apply {
        FilePickerSelectOptions.maxCount = maxCount
    }

    fun singleChoose() = apply {
        FilePickerSelectOptions.maxCount = 1
    }


    fun onlyShowCommonly() = apply {
        FilePickerSelectOptions.onlyShowCommonly = true
    }

    fun onlyShowBrowser() = apply {
        FilePickerSelectOptions.onlyShowBrowser = true
    }

    fun fileTypes(vararg fileType: String) = apply {
        FilePickerSelectOptions.fileTypes = fileType
    }

    fun sortType(sortType: FilePickerSortType) = apply {
        FilePickerSelectOptions.sortType = sortType
    }

    fun start(listener: () -> Unit) {
        val permission =
            if (filePicker.getActivity() != null) XXPermissions.with(filePicker.getActivity())
            else XXPermissions.with(filePicker.getFragment())
        permission.permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request { _, all ->
                if (!all) return@request
                FilePickerSelectOptions.onSelectFileListener = listener
                filePicker.getFragment()?.startActivity(FilePickerSelectFileActivity::class)
                    ?: filePicker.getActivity()?.startActivity(FilePickerSelectFileActivity::class)

            }
    }

}