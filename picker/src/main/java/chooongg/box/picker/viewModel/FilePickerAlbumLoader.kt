package chooongg.box.picker.viewModel

import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.MediaStore
import androidx.loader.content.CursorLoader
import chooongg.box.picker.FilePickerSelectOptions

class FilePickerAlbumLoader(
    context: Context,
    selection: String?,
    selectionArgs: Array<out String>?,
) : CursorLoader(
    context,
    MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL), arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.BUCKET_ID,
        MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
        "COUNT(*) AS $COLUMN_COUNT"
    ),
    selection,
    selectionArgs,
    "${MediaStore.MediaColumns.DATE_TAKEN} DESC"
) {
    companion object {

        const val COLUMN_COUNT = "count"

        private val COLUMNS = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.MediaColumns.BUCKET_ID,
            MediaStore.MediaColumns.BUCKET_DISPLAY_NAME,
            COLUMN_COUNT
        )

        private const val SELECTION_FOR_SINGLE_MEDIA_TYPE =
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? " +
                    "AND ${MediaStore.MediaColumns.SIZE}>0" +
                    ") GROUP BY (${MediaStore.MediaColumns.BUCKET_ID}"

        fun newInstance(context: Context): CursorLoader {
            val selection: String
            val selectionArgs: Array<String>
            when {
                FilePickerSelectOptions.onlyShowImages -> {
                    selection = SELECTION_FOR_SINGLE_MEDIA_TYPE
                    selectionArgs = arrayOf(
                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString()
                    )
                }
                FilePickerSelectOptions.onlyShowVideos -> {
                    selection = SELECTION_FOR_SINGLE_MEDIA_TYPE
                    selectionArgs = arrayOf(
                        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
                    )
                }
                else -> {
                    selection = SELECTION_FOR_SINGLE_MEDIA_TYPE
                    selectionArgs = arrayOf(
                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                        MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
                    )
                }
            }
            return FilePickerAlbumLoader(context, selection, selectionArgs)
        }
    }

    override fun loadInBackground(): Cursor? {
        val albums = super.loadInBackground()
        val allAlbum = MatrixCursor(COLUMNS)
        var totalCount = 0
        var allAlbumCoverId = -1
        if (albums != null) {
            while (albums.moveToNext()) {
                totalCount += albums.getInt(albums.getColumnIndex(COLUMN_COUNT))
            }
            if (albums.moveToFirst()) {
                allAlbumCoverId = albums.getInt(albums.getColumnIndex(MediaStore.MediaColumns._ID))
            }
        }
        allAlbum.addRow(arrayOf(Album))
    }
}