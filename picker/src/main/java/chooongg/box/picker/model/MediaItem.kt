package chooongg.box.picker.model

import android.net.Uri
import android.os.Parcelable
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.coroutineScope
import chooongg.box.ext.launchIO
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
    val id: Long,
    val uri: Uri?,
    var compressPath: String?,
    var httpPath: String?,
    val isVideo: Boolean,
    val videoDuration: Long,
    val mimeType: String?,
    var state: Int = 0
) : Parcelable {

    companion object {
        const val STATE_COMPLETE = 0
        const val STATE_COMPRESSING = 1
        const val STATE_COMPRESS_FAIL = 2
        const val STATE_UPLOADING = 3
        const val STATE_UPLOAD_FAIL = 4
    }

    @IgnoredOnParcel
    private var compressListener: ((isComplete: Boolean) -> Unit)? = null

    fun beginOperate(lifecycleOwner: LifecycleOwner, block: (state: Int) -> Unit) {
        lifecycleOwner.lifecycle.coroutineScope.launchIO {
            state = STATE_COMPRESSING
            block.invoke(state)

        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is MediaItem) {
            if (this.id == other.id) return true
        }
        return false
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (uri?.hashCode() ?: 0)
        result = 31 * result + (compressPath?.hashCode() ?: 0)
        result = 31 * result + (httpPath?.hashCode() ?: 0)
        result = 31 * result + isVideo.hashCode()
        result = 31 * result + videoDuration.hashCode()
        return result
    }
}