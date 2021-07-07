package chooongg.box.picker.viewmodels

import android.content.ContentUris
import android.database.ContentObserver
import android.database.Cursor
import android.provider.BaseColumns
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chooongg.box.ext.APP
import chooongg.box.picker.FilePickerConst
import chooongg.box.picker.FilePickerManager
import chooongg.box.picker.models.Media
import chooongg.box.picker.models.PhotoDirectory
import chooongg.box.picker.utils.registerObserver
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPickerViewModel : ViewModel() {

    private val _lvMediaData = MutableLiveData<List<Media>>()
    val lvMediaData: LiveData<List<Media>>
        get() = _lvMediaData

    private val _lvPhotoDirsData = MutableLiveData<List<PhotoDirectory>>()
    val lvPhotoDirsData: LiveData<List<PhotoDirectory>>
        get() = _lvPhotoDirsData

    private val _lvDataChanged = MutableLiveData<Boolean>()
    val lvDataChanged: LiveData<Boolean>
        get() = _lvDataChanged

    private var contentObserver: ContentObserver? = null

    private fun registerContentObserver() {
        if (contentObserver == null) {
            contentObserver = APP.contentResolver.registerObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            ) {
                _lvDataChanged.value = true
            }
        }
    }

    fun getMedia(
        bucketId: String? = null,
        mediaType: Int = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
        imageFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE,
        videoFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE
    ) {
        viewModelScope.launch {
            try {
                val medias = mutableListOf<Media>()
                queryImages(bucketId, mediaType, imageFileSize, videoFileSize).map { dir ->
                    medias.addAll(dir.medias)
                }
                medias.sortWith { a, b -> (b.id - a.id).toInt() }
                _lvMediaData.postValue(medias)
                registerContentObserver()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun getPhotoDirs(
        bucketId: String? = null,
        mediaType: Int = MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE,
        imageFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE,
        videoFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE
    ) {
        viewModelScope.launch {
            try {
                val dirs = queryImages(bucketId, mediaType, imageFileSize, videoFileSize)
                val photoDirectory = PhotoDirectory()
                photoDirectory.bucketId = null
                when (mediaType) {
                    MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO -> {
                        photoDirectory.name = "全部视频"
                    }
                    MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE -> {
                        photoDirectory.name = "全部图片"
                    }
                    else -> {
                        photoDirectory.name = "全部"
                    }
                }
                if (dirs.isNotEmpty() && dirs[0].medias.size > 0) {
                    photoDirectory.dateAdded = dirs[0].dateAdded
                    photoDirectory.setCoverPath(dirs[0].medias[0].uri)
                }
                for (i in dirs.indices) {
                    photoDirectory.medias.addAll(dirs[i].medias)
                }
                dirs.add(0, photoDirectory)
                _lvPhotoDirsData.postValue(dirs)
                registerContentObserver()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @WorkerThread
    suspend fun queryImages(
        bucketId: String?,
        mediaType: Int,
        imageFileSize: Int,
        videoFileSize: Int
    ): MutableList<PhotoDirectory> {
        var data = mutableListOf<PhotoDirectory>()
        withContext(Dispatchers.IO) {
            val projection = null
            val uri = MediaStore.Files.getContentUri("external")
            val sortOrder = MediaStore.Images.Media._ID + " DESC"
            val selectionArgs = mutableListOf<String>()

            var selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                    + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)

            if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                selection = (MediaStore.Files.FileColumns.MEDIA_TYPE + "="
                        + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO)

                if (videoFileSize != FilePickerConst.DEFAULT_FILE_SIZE) {
                    selection += " AND ${MediaStore.Video.Media.SIZE}<=?"
                    selectionArgs.add("${videoFileSize * 1024 * 1024}")
                }
            }

            if (mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE && imageFileSize != FilePickerConst.DEFAULT_FILE_SIZE) {
                selection += " AND ${MediaStore.Images.Media.SIZE}<=?"
                selectionArgs.add("${imageFileSize * 1024 * 1024}")
            }

            if (!FilePickerManager.isShowGif) {
                selection += " AND " + MediaStore.Images.Media.MIME_TYPE + "!='" + MimeTypeMap.getSingleton()
                    .getMimeTypeFromExtension("gif") + "'"
            }

            if (bucketId != null)
                selection += " AND " + MediaStore.Images.Media.BUCKET_ID + "='" + bucketId + "'"

            val cursor = APP.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs.toTypedArray(),
                sortOrder
            )

            if (cursor != null) {
                data = getPhotoDirectories(mediaType, cursor)
                cursor.close()
            }
        }
        return data
    }

    @WorkerThread
    private fun getPhotoDirectories(fileType: Int, data: Cursor): MutableList<PhotoDirectory> {
        val directories = mutableListOf<PhotoDirectory>()

        while (data.moveToNext()) {

            val imageId = data.getLong(data.getColumnIndexOrThrow(BaseColumns._ID))
            val bucketId =
                data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_ID))
            val name =
                data.getString(data.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
            val fileName = data.getString(data.getColumnIndexOrThrow(MediaStore.MediaColumns.TITLE))
            val mediaType =
                data.getInt(data.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE))

            val photoDirectory = PhotoDirectory()
            photoDirectory.id = imageId
            photoDirectory.bucketId = bucketId
            photoDirectory.name = name

            var contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                imageId
            )
            if (fileType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) {
                contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    imageId
                )
            }
            if (!directories.contains(photoDirectory)) {
                photoDirectory.addPhoto(imageId, fileName, contentUri, mediaType)
                photoDirectory.dateAdded =
                    data.getLong(data.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                directories.add(photoDirectory)
            } else {
                directories[directories.indexOf(photoDirectory)]
                    .addPhoto(imageId, fileName, contentUri, mediaType)
            }
        }

        return directories
    }

    override fun onCleared() {
        contentObserver?.apply {
            APP.contentResolver.unregisterContentObserver(this)
        }
    }
}