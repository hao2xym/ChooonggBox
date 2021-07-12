package chooongg.box.picker.viewModel

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
import chooongg.box.picker.model.MediaItem
import java.io.File
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

            var cursor: Cursor? = null
            try {
                cursor = APP.contentResolver.query(
                    contentUri,
                    projection,
                    getSelection(),
                    getSelectionArgs(),
                    "datetaken DESC"
                )
                while (cursor!!.moveToNext()) {
                    val bucketId =
                        cursor.getLong(cursor.getColumnIndexOrThrow("bucket_id"))
                    if (!albumIds.contains(bucketId)) {
                        albumIds.add(bucketId)
                        val bucketName =
                            cursor.getString(cursor.getColumnIndexOrThrow("bucket_display_name"))
                        val mediaType =
                            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                        val data =
                            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
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
                                    File(data),
                                    mediaType
                                )
                            )
                        }
                        albums.add(
                            AlbumDirector(
                                bucketId,
                                bucketName,
                                getBucketCount(
                                    contentUri,
                                    getSelection(),
                                    getSelectionArgs(),
                                    bucketId
                                ),
                                File(data),
                                mediaType
                            )
                        )
                    }
                }
                withMain { onMediaListener?.onAlbumLoaded(albums) }
            } catch (e: Exception) {
                if (e is CancellationException) return@launchIO
                e.printStackTrace()
                albums.clear()
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
                        0,
                        File(""),
                        MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE
                    )
                )
                withMain { onMediaListener?.onAlbumLoaded(albums) }
            } finally {
                cursor?.close()
            }
        }
    }

    fun getMedia(bucketId: Long?) {
        viewModelScope.launchIO {
            val mediaData = ArrayList<MediaItem>()
            val contentUri = MediaStore.Files.getContentUri("external")
            val projection = arrayOf(
                MediaStore.Files.FileColumns._ID,
                MediaStore.Files.FileColumns.MEDIA_TYPE,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.DATA
            )
            var cursor: Cursor? = null
            try {
                val selection = if (bucketId == null) {
                    getSelection()
                } else {
                    getSelection() + " AND ${"bucket_id"}=${bucketId}"
                }
                cursor = APP.contentResolver.query(
                    contentUri,
                    projection,
                    selection,
                    getSelectionArgs(),
                    "datetaken DESC"
                )
                while (cursor!!.moveToNext()) {
                    val id =
                        cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
                    val mediaType =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))
                    val mimeType =
                        cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                    val data =
                        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
                    if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                        mediaData.add(
                            MediaItem(id, data, null, null, true, getVideoDuration(contentUri, id))
                        )
                    } else {
                        mediaData.add(MediaItem(id, data, null, null, false, 0))
                    }
                }
                withMain { onMediaListener?.onMediaLoaded(mediaData) }
            } catch (e: Exception) {
                if (e is CancellationException) return@launchIO
                withMain { onMediaListener?.onMediaLoadError(e) }
            } finally {
                cursor?.close()
            }
        }
    }

    private fun getVideoDuration(
        contentUri: Uri,
        id: Long
    ): Long {
        var cursor: Cursor? = null
        var duration = 0L
        try {
            cursor = APP.contentResolver.query(
                contentUri,
                arrayOf(MediaStore.Video.Media.DURATION),
                "${MediaStore.Files.FileColumns._ID}=?",
                arrayOf(id.toString()),
                null
            )
            if (cursor!!.moveToFirst()) {
                duration =
                    cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
            }

        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return duration
    }

    private fun getBucketCount(
        contentUri: Uri,
        selection: String,
        selectionArgs: Array<String>,
        bucketId: Long
    ): Int {
        var cursor: Cursor? = null
        var count = 0
        try {
            cursor = APP.contentResolver.query(
                contentUri,
                arrayOf(MediaStore.Files.FileColumns._ID),
                selection + " AND ${"bucket_id"}=${bucketId}",
                selectionArgs,
                null
            )
            count = cursor?.count ?: 0
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return count
    }

    private fun getSelection() = when {
        FilePickerSelectOptions.onlyShowImages && FilePickerSelectOptions.showGif ->
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? AND ${MediaStore.MediaColumns.SIZE}>0"
        FilePickerSelectOptions.onlyShowImages ->
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? AND ${MediaStore.Files.FileColumns.MIME_TYPE}!=? AND ${MediaStore.MediaColumns.SIZE}>0"
        FilePickerSelectOptions.onlyShowVideos ->
            "${MediaStore.Files.FileColumns.MEDIA_TYPE}=? AND ${MediaStore.MediaColumns.SIZE}>0"
        FilePickerSelectOptions.showGif ->
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) AND ${MediaStore.MediaColumns.SIZE}>0"
        else ->
            "(${MediaStore.Files.FileColumns.MEDIA_TYPE}=? OR ${MediaStore.Files.FileColumns.MEDIA_TYPE}=?) AND ${MediaStore.Files.FileColumns.MIME_TYPE}!=? AND ${MediaStore.MediaColumns.SIZE}>0"
    }


    private fun getSelectionArgs() = when {
        FilePickerSelectOptions.onlyShowImages && FilePickerSelectOptions.showGif ->
            arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString())
        FilePickerSelectOptions.onlyShowImages ->
            arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(), "image/gif")
        FilePickerSelectOptions.onlyShowVideos ->
            arrayOf(MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString())
        FilePickerSelectOptions.showGif -> arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString()
        )
        else -> arrayOf(
            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE.toString(),
            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO.toString(),
            "image/gif"
        )
    }

    interface OnMediaListener {
        fun onAlbumLoaded(albums: ArrayList<AlbumDirector>)
        fun onMediaLoaded(mediaData: ArrayList<MediaItem>)
        fun onMediaLoadError(e: Exception)
    }
}