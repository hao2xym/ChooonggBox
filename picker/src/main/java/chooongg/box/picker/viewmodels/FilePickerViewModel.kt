package chooongg.box.picker.viewmodels

import android.content.Context
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import chooongg.box.ext.withIO

class FilePickerViewModel(private val context: Context) : ViewModel() {

    suspend fun getAllFile() {
        withIO {
            val cursor = context.contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                arrayOf(
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.DATE_ADDED,
                    MediaStore.Files.FileColumns.TITLE
                ), null, null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            )
            if (cursor != null) {
                when(cursor.moveToNext()){

                }
                cursor.close()
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}