package chooongg.box.picker.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class CommonlyDirectory(
    val name: String,
    val packageName: String,
    val paths: Array<String>
) : Parcelable {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CommonlyDirectory
        if (name != other.name) return false
        if (!paths.contentEquals(other.paths)) return false
        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + paths.contentHashCode()
        return result
    }
}
