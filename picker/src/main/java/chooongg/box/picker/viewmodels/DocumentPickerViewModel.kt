package chooongg.box.picker.viewmodels

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.provider.BaseColumns
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.WorkerThread
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chooongg.box.ext.APP
import chooongg.box.ext.withIO
import chooongg.box.picker.models.Document
import chooongg.box.picker.models.PickerFileType
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class DocumentPickerViewModel() : ViewModel() {

    fun getDocuments(fileTypes: List<PickerFileType>, comparator: Comparator<Document>?) {
        viewModelScope.launch {
            queryDocuments(fileTypes, comparator)
        }
    }

    suspend fun queryDocuments(
        fileTypes: List<PickerFileType>,
        comparator: Comparator<Document>?
    ): HashMap<PickerFileType, List<Document>> {
        withIO {
            val cursor = APP.contentResolver.query(
                MediaStore.Files.getContentUri("external"),
                arrayOf(
                    MediaStore.Files.FileColumns._ID,
                    MediaStore.Files.FileColumns.MIME_TYPE,
                    MediaStore.Files.FileColumns.SIZE,
                    MediaStore.Files.FileColumns.DATE_ADDED,
                    MediaStore.Files.FileColumns.TITLE
                ),
                "${MediaStore.Files.FileColumns.MEDIA_TYPE}!=${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE}" +
                        " AND ${MediaStore.Files.FileColumns.MEDIA_TYPE}!=${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO}",
                null,
                MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            )
            if (cursor != null) {
                when (cursor.moveToNext()) {

                }
                cursor.close()
            }
        }
        return HashMap<PickerFileType, List<Document>>()
    }

    @WorkerThread
    private fun getDocumentFromCursor(data: Cursor): MutableList<Document> {
        val documents = mutableListOf<Document>()
        while (data.moveToNext()) {
            val imageId = data.getLong(data.getColumnIndexOrThrow(BaseColumns._ID))
            val path = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
            val title =
                data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
            if (path != null) {
                val fileType = getFileType(PickerManager.getFileTypes(), path)
                val file = File(path)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Files.getContentUri("external"),
                    imageId
                )
                val photoUri: Uri = Uri.withAppendedPath(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    data.getString(data.getColumnIndexOrThrow(BaseColumns._ID))
                )
                if (fileType != null && !file.isDirectory && file.exists()) {
                    val document = Document(
                        imageId, title, if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            MediaStore.setRequireOriginal(photoUri)
                        } else contentUri
                    )
                    document.fileType = fileType
                    val mimeType =
                        data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                    if (mimeType != null && !TextUtils.isEmpty(mimeType)) {
                        document.mimeType = mimeType
                    } else {
                        document.mimeType = ""
                    }
                    document.size =
                        data.getString(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
                    if (!documents.contains(document)) documents.add(document)
                }
            }
        }
        return documents
    }

    private fun getFileType(types: ArrayList<PickerFileType>, path: String): PickerFileType? {
        for (index in types.indices) {
            for (string in types[index].extensions) {
                if (path.endsWith(string)) return types[index]
            }
        }
        return null
    }
}