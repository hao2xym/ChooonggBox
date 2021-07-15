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
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is MediaItem) {
            if (this.id == other.id) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (path?.hashCode() ?: 0)
        result = 31 * result + (compressPath?.hashCode() ?: 0)
        result = 31 * result + (httpPath?.hashCode() ?: 0)
        result = 31 * result + isVideo.hashCode()
        result = 31 * result + videoDuration.hashCode()
        return result
    }
}