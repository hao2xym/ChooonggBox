package chooongg.box.picker.models

import android.net.Uri
import android.os.Parcelable
import androidx.annotation.IntDef
import kotlinx.parcelize.Parcelize

@Parcelize
open class BaseFile(
    open val id: Long,
    open val name: String,
    open var uri: Uri? = null,
    open var url: String? = null,
    open var mimeType: String = "",
    @Status open var status: Int = STATUS_NORMAL
) : Parcelable {

    @IntDef(
        STATUS_NORMAL,
        STATUS_COMPRESSING, STATUS_COMPRESS_FAILED,
        STATUS_UPLOADING, STATUS_UPLOAD_FAILED
    )
    annotation class Status

    companion object {
        const val STATUS_NORMAL = 0
        const val STATUS_COMPRESSING = 1
        const val STATUS_COMPRESS_FAILED = 2
        const val STATUS_UPLOADING = 3
        const val STATUS_UPLOAD_FAILED = 4
    }
}