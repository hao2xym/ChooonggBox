package chooongg.box.picker

import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.annotation.DrawableRes
import chooongg.box.picker.models.PickerFileType
import chooongg.box.picker.models.sort.SortingTypes
import java.util.ArrayList
import java.util.LinkedHashSet

object FilePickerManager {

    private val fileTypes: LinkedHashSet<PickerFileType> = LinkedHashSet()

    val selectedPhotos: ArrayList<Uri> = ArrayList()

    val selectedFiles: ArrayList<Uri> = ArrayList()

    @DrawableRes
    var cameraIcon = R.drawable.layer_camera

    var sortingType = SortingTypes.NONE

    var imageFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE
    var videoFileSize: Int = FilePickerConst.DEFAULT_FILE_SIZE

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
        val pdfs = arrayOf("pdf")
        fileTypes.add(PickerFileType(FilePickerConst.PDF, pdfs, R.drawable.icon_file_pdf))

        val docs = arrayOf("doc", "docx", "dot", "dotx")
        fileTypes.add(PickerFileType(FilePickerConst.DOC, docs, R.drawable.icon_file_doc))

        val ppts = arrayOf("ppt", "pptx")
        fileTypes.add(PickerFileType(FilePickerConst.PPT, ppts, R.drawable.icon_file_ppt))

        val xlss = arrayOf("xls", "xlsx")
        fileTypes.add(PickerFileType(FilePickerConst.XLS, xlss, R.drawable.icon_file_xls))

        val txts = arrayOf("txt")
        fileTypes.add(PickerFileType(FilePickerConst.TXT, txts, R.drawable.icon_file_unknown))
    }

    fun getFileTypes(): ArrayList<FileType> {
        return ArrayList(fileTypes)
    }
}