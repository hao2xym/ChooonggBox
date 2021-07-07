package chooongg.box.picker

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.annotation.DrawableRes
import chooongg.box.picker.models.Document
import chooongg.box.picker.models.Media
import chooongg.box.picker.models.PickerFileType
import chooongg.box.picker.models.sort.SortingTypes
import java.util.*
import kotlin.collections.ArrayList

object FilePickerManager {

    internal var mediaResultListener : ((List<Media>)->Unit)? = null
    internal var documentResultListener : ((List<Document>)->Unit)? = null

    var theme: Int = R.style.BoxTheme_Picker

    private val fileTypes: LinkedHashSet<PickerFileType> = LinkedHashSet()

    internal var maxCount = FilePickerConst.DEFAULT_MAX_COUNT

    val selectedPhotos: ArrayList<Uri> = ArrayList()

    val selectedFiles: ArrayList<Uri> = ArrayList()

    val currentCount: Int
        get() = selectedPhotos.size + selectedFiles.size

    @DrawableRes
    var cameraIcon = R.drawable.layer_camera

    internal var isEnableCamera = true

    internal var isShowGif = false

    internal var isShowVideos: Boolean = false

    internal var isShowFolderView = true

    internal var sortingType = SortingTypes.NONE

    internal var imageFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE
    internal var videoFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE

    /**
     * The preferred screen orientation this activity would like to run in.
     * From the {@link android.R.attr#screenOrientation} attribute, one of
     * {@link #SCREEN_ORIENTATION_UNSPECIFIED},
     * {@link #SCREEN_ORIENTATION_LANDSCAPE},
     * {@link #SCREEN_ORIENTATION_PORTRAIT},
     * {@link #SCREEN_ORIENTATION_USER},
     * {@link #SCREEN_ORIENTATION_BEHIND},
     * {@link #SCREEN_ORIENTATION_SENSOR},
     * {@link #SCREEN_ORIENTATION_NOSENSOR},
     * {@link #SCREEN_ORIENTATION_SENSOR_LANDSCAPE},
     * {@link #SCREEN_ORIENTATION_SENSOR_PORTRAIT},
     * {@link #SCREEN_ORIENTATION_REVERSE_LANDSCAPE},
     * {@link #SCREEN_ORIENTATION_REVERSE_PORTRAIT},
     * {@link #SCREEN_ORIENTATION_FULL_SENSOR},
     * {@link #SCREEN_ORIENTATION_USER_LANDSCAPE},
     * {@link #SCREEN_ORIENTATION_USER_PORTRAIT},
     * {@link #SCREEN_ORIENTATION_FULL_USER},
     * {@link #SCREEN_ORIENTATION_LOCKED},
     */
    var orientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
        get() = field

    fun addFileType(fileType: PickerFileType) {
        fileTypes.add(fileType)
    }

    fun addDocTypes() {
        fileTypes.add(
            PickerFileType("安装包", arrayOf("apk"), R.drawable.ic_picker_apk)
        )
        fileTypes.add(
            PickerFileType("音频", arrayOf("mp3", "wav"), R.drawable.ic_picker_audio)
        )
        fileTypes.add(
            PickerFileType("文档", arrayOf("doc", "docx", "dot", "dotx"), R.drawable.ic_picker_doc)
        )
        fileTypes.add(
            PickerFileType("网页", arrayOf("html"), R.drawable.ic_picker_html)
        )
        fileTypes.add(
            PickerFileType("PDF", arrayOf("pdf"), R.drawable.ic_picker_pdf)
        )
        fileTypes.add(
            PickerFileType("PPT", arrayOf("ppt", "pptx"), R.drawable.ic_picker_ppt)
        )
        fileTypes.add(
            PickerFileType("文本", arrayOf("txt"), R.drawable.ic_picker_txt)
        )
        fileTypes.add(
            PickerFileType("表格", arrayOf("xls", "xlsx"), R.drawable.ic_picker_xls)
        )
        fileTypes.add(
            PickerFileType("压缩包", arrayOf("zip", "rar", "7z"), R.drawable.ic_picker_zip)
        )
    }

    fun add(path: Uri?, isMedia: Boolean) {
        if (path != null && shouldAdd()) {
            if (!selectedPhotos.contains(path) && isMedia) {
                selectedPhotos.add(path)
            } else if (!selectedFiles.contains(path)) {
                selectedFiles.add(path)
            } else {
                return
            }
        }
    }

    fun add(paths: List<Uri>, isMedia: Boolean) {
        for (index in paths.indices) {
            add(paths[index], isMedia)
        }
    }

    fun remove(path: Uri?, isMedia: Boolean) {
        if (isMedia && selectedPhotos.contains(path)) {
            selectedPhotos.remove(path)
        } else {
            selectedFiles.remove(path)
        }
    }

    fun shouldAdd(): Boolean {
        return if (maxCount == -1) true else currentCount < maxCount
    }

    fun reset() {
        selectedFiles.clear()
        selectedPhotos.clear()
        fileTypes.clear()
        maxCount = -1
    }

    fun clearSelections() {
        selectedPhotos.clear()
        selectedFiles.clear()
    }

    fun getFileTypes(): ArrayList<PickerFileType> {
        return ArrayList(fileTypes)
    }
}