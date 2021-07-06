package chooongg.box.picker.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class PhotoDirectory(
    var id: Long = 0,
    var bucketId: String? = null,
    private var coverUri: Uri? = null,
    var name: String? = null,
    var dateAdded: Long = 0,
    val medias: MutableList<Media> = mutableListOf()
) : Parcelable {

    fun getCoverUri(): Uri? {
        return when {
            medias.size > 0 -> medias[0].uri
            coverUri != null -> coverUri
            else -> null
        };
    }

    fun setCoverPath(coverPath: Uri?) {
        this.coverUri = coverPath
    }

    fun addPhoto(imageId: Long, fileName: String, uri: Uri, mediaType: Int) {
        medias.add(Media(imageId, fileName, uri, mediaType = mediaType))
    }

    override fun equals(other: Any?): Boolean {
        return this.bucketId == (other as? PhotoDirectory)?.bucketId
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + (bucketId?.hashCode() ?: 0)
        result = 31 * result + (coverUri?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + dateAdded.hashCode()
        result = 31 * result + medias.hashCode()
        return result
    }
}