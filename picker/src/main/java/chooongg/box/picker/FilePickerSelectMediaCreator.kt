package chooongg.box.picker

import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import chooongg.box.ext.startActivity
import chooongg.box.picker.activity.FilePickerSelectMediaActivity
import chooongg.box.picker.model.MediaItem
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class FilePickerSelectMediaCreator(
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

    fun isNumberSelect(isNumberSelect: Boolean) = apply {
        FilePickerSelectOptions.isNumberSelect = isNumberSelect
    }

    fun needPreview(isNeed: Boolean) = apply {
        FilePickerSelectOptions.needPreview = isNeed
    }

    fun needCamera(isNeed: Boolean) = apply {
        FilePickerSelectOptions.needCamera = isNeed
    }

    fun needCrop(isNeed: Boolean) = apply {
        FilePickerSelectOptions.needCrop = isNeed
    }

    fun showGif(enable: Boolean) = apply {
        FilePickerSelectOptions.showGif = enable
    }

    fun onlyShowImages() = apply {
        FilePickerSelectOptions.onlyShowImages = true
    }

    fun onlyShowVideos() = apply {
        FilePickerSelectOptions.onlyShowVideos = true
    }

    fun compressImage(enable: Boolean) = apply {
        FilePickerSelectOptions.compressImage = enable
    }

    fun start(listener: (ArrayList<MediaItem>) -> Unit) {
        val permission =
            if (filePicker.getActivity() != null) XXPermissions.with(filePicker.getActivity())
            else XXPermissions.with(filePicker.getFragment())
        permission.permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request { _, all ->
                if (!all) return@request
                FilePickerSelectOptions.onSelectMediaListener = listener
                filePicker.getFragment()?.startActivity(FilePickerSelectMediaActivity::class)
                    ?: filePicker.getActivity()?.startActivity(FilePickerSelectMediaActivity::class)
            }
    }

}