package chooongg.box.picker.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumDirector(
    val id: Long?,
    val name: String,
    val count: Int,
    val coverUri: Uri
) : Parcelable