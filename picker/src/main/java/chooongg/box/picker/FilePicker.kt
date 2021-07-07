package chooongg.box.picker

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import chooongg.box.picker.activity.FilePickerMediaActivity
import chooongg.box.picker.models.sort.SortingTypes
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import java.util.*

object FilePicker {

    private val mPickerOptionsBundle: Bundle = Bundle()

    class Builder() {

        init {
            mPickerOptionsBundle.clear()
            FilePickerManager.reset()
        }

        fun setImageSizeLimit(fileSize: Int) = apply {
            FilePickerManager.imageFileSize = fileSize
        }

        fun setVideoSizeLimit(fileSize: Int) = apply {
            FilePickerManager.videoFileSize = fileSize
        }

        fun setMaxCount(maxCount: Int) = apply {
            FilePickerManager.maxCount = maxCount
        }

        fun setActivityTheme(theme: Int) = apply {
            FilePickerManager.theme = theme
        }

        fun setSelectedFiles(selectedPhotos: ArrayList<Uri>) = apply {
            mPickerOptionsBundle.putParcelableArrayList(
                FilePickerConst.KEY_SELECTED_MEDIA,
                selectedPhotos
            )
        }

        fun enableVideoPicker(status: Boolean) = apply {
            FilePickerManager.isShowVideos = status
        }

        fun showGifs(status: Boolean) = apply {
            FilePickerManager.isShowGif = status
        }

        fun showFolderView(status: Boolean) = apply {
            FilePickerManager.isShowFolderView = status
        }

        fun sortDocumentsBy(type: SortingTypes) = apply {
            FilePickerManager.sortingType = type
        }

        fun pickMedia(context: Context) {
            XXPermissions.with(context).permission(Permission.Group.STORAGE)
                .request { permissions, all ->
                    if (!all) return@request
                    context.startActivity(
                        Intent(
                            context, FilePickerMediaActivity::class.java
                        ).apply {
                            putExtras(mPickerOptionsBundle)
                        })
                }
        }

        fun pickMedia(fragment: Fragment) {
        }

        fun pickDocument(context: Context) {
        }
    }
}