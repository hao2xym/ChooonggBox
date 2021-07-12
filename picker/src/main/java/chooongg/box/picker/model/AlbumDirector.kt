package chooongg.box.picker.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class AlbumDirector(
    val id: Long?,
    val name: String,
    val count: Int,
    val coverPath: File,
    val coverMediaType: Int
) : Parcelable