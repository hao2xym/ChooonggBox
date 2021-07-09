package chooongg.box.picker.viewModel

import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import chooongg.box.ext.APP
import chooongg.box.picker.FilePickerSelectOptions

class FilePickerAlbumViewModel : ViewModel() {

    companion object {

        const val COLUMN_COUNT = "count"
    }

    fun getAlbum() {
        val uri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL)
        val projection = arrayOf(
            MediaStore.Files.FileColumns.BUCKET_ID,
            MediaStore.Files.FileColumns.BUCKET_DISPLAY_NAME
        )
        val selection: String
        val selectionArgs: Array<String>
        when {
            FilePickerSelectOptions.onlyShowImages -> {
                if (FilePickerSelectOptions.showGif) {
                    selection = ""
                }else{

                }
            }
            FilePickerSelectOptions.onlyShowVideos -> {

            }
            FilePickerSelectOptions.showGif -> {

            }
            else -> {

            }
        }

        APP.contentResolver.query(uri, projection)
    }

}