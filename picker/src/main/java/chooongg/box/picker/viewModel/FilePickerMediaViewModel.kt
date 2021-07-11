package chooongg.box.picker.viewModel

import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chooongg.box.ext.APP
import chooongg.box.ext.launchIO
import chooongg.box.ext.resourcesString
import chooongg.box.ext.withMain
import chooongg.box.picker.FilePickerSelectOptions
import chooongg.box.picker.R
import chooongg.box.picker.model.AlbumDirector
import kotlin.coroutines.cancellation.CancellationException

class FilePickerMediaViewModel : ViewModel() {

    private var onMediaListener: OnMediaListener? = null

    fun setOnGetAlbumListener(onMediaListener: OnMediaListener) {
        this.onMediaListener = onMediaListener
    }

    fun getAlbum() {
        viewModelScope.launchIO {
            val albumIds = ArrayList<Long>()
            val albums = ArrayList<AlbumDirector>()
            val contentUri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                "bucket_id",
                "bucket_display_name",
                MediaStore.Files.FileColumns.DATA
            )
            val selection: String
            val selectionArgs: Array<String>
            when {
                FilePickerSelectOptions.onlyShowImages -> {
                    if (FilePickerSelectOptions.showGif) {
                        selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" +
                                " AND ${MediaStore.MediaColumns.SIZE}>0"
                        selectionArgs =
                            arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
                    } else {
                        selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" +
                                " AND ${MediaStore.Files.FileColumns.MIME_TYPE}!=?" +
                                " AND ${MediaStore.MediaColumns.SIZE}>0"
                        selectionArgs =
                            arrayOf(
                                MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                                "image/gif"
                            )
                    }
                }
                FilePickerSelectOptions.onlyShowVideos -> {
                    selection = "${MediaStore.Files.FileColumns.MEDIA_TYPE}=?" +
                            " AND ${MediaStore.MediaColumns.SIZE}>0"
                    selectionArgs =
                        arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
                }
                FilePickerSelectOptions.showGif -> {
                    selection =
                        "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?)" +
                                " AND ${MediaStore.MediaColumns.SIZE}>0"
                    selectionArgs =
                        arrayOf(
                            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
                        )
                }
                else -> {
                    selection =
                        "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?)" +
                                " AND ${MediaStore.Files.FileColumns.MIME_TYPE}!=?" +
                                " AND ${MediaStore.MediaColumns.SIZE}>0"
                    selectionArgs =
                        arrayOf(
                            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
                            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
                            "image/gif"
                        )
                }
            }

            var cursor: Cursor? = null
            try {
                cursor = APP.contentResolver.query(
                    contentUri,
                    projection,
                    selection,
                    selectionArgs,
                    "${"datetaken"} DESC"
                )
                while (cursor!!.moveToNext()) {
                    val bucketId =
                        cursor.getLong(cursor.getColumnIndexOrThrow("bucket_id"))
                    if (!albumIds.contains(bucketId)) {
                        albumIds.add(bucketId)
                        val bucketName =
                            cursor.getString(cursor.getColumnIndexOrThrow("bucket_display_name"))
                        val id =
                            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                        val mimeType =
                            cursor.getInt(cursor.getColumnIndex(MediaStore.Files.FileColumns.MEDIA_TYPE))
                        val typeContentUri =
                            if (mimeType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE) {
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                            } else {
                                MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                            }
                        val coverUri = ContentUris.withAppendedId(typeContentUri, id)
                        if (albums.isEmpty()) {
                            albums.add(
                                AlbumDirector(
                                    null,
                                    APP.resourcesString(
                                        when {
                                            FilePickerSelectOptions.onlyShowImages -> R.string.picker_media_all_image
                                            FilePickerSelectOptions.onlyShowVideos -> R.string.picker_media_all_video
                                            else -> R.string.picker_media_all_image_video
                                        }
                                    ),
                                    cursor.count,
                                    coverUri
                                )
                            )
                        }
                        albums.add(
                            AlbumDirector(
                                bucketId,
                                bucketName,
                                getBucketCount(contentUri, selection, selectionArgs, bucketId),
                                coverUri
                            )
                        )
                    }
                }
                withMain { onMediaListener?.onAlbumLoaded(albums) }
            } catch (e: Exception) {
                if (e is CancellationException) return@launchIO
                e.printStackTrace()
                withMain { onMediaListener?.onAlbumError(e) }
            } finally {
                cursor?.close()
            }
        }
    }

    fun getMedia(bucketId: String?) {
        viewModelScope.launchIO {
            val contentUri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
            )
            try {

            } catch (e: Exception) {

            } finally {

            }
        }
    }


    private fun getBucketCount(
        contentUri: Uri,
        selection: String,
        selectionArgs: Array<String>,
        bucketId: Long
    ): Int {
        val cursor = APP.contentResolver.query(
            contentUri,
            arrayOf(MediaStore.Files.FileColumns._ID),
            selection + " AND ${"bucket_id"}=${bucketId}",
            selectionArgs,
            null
        ) ?: return 0
        val count = cursor.count
        cursor.close()
        return count
    }

    interface OnMediaListener {
        fun onAlbumLoaded(albums: ArrayList<AlbumDirector>)
        fun onAlbumError(e: Exception)
    }
}