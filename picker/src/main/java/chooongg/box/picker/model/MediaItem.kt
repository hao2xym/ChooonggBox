package chooongg.box.picker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
    val id: Long,
    val path: String?,
    var compressPath: String?,
    var httpPath: String?,
    val isVideo: Boolean,
    val videoDuration: Long
) : Parcelable