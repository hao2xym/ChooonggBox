package chooongg.box.picker.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
    val id: Long,
    val uri: Uri,
    val path: String,
    var compressPath: String?,
    var httpPath: String?,
    val isVideo: Boolean,
    val thumbnailUri: Uri?,
    val thumbnail: String?
) : Parcelable