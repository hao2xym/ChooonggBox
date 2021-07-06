package chooongg.box.picker.models

import android.net.Uri
import kotlinx.parcelize.Parcelize

@Parcelize
class Media @JvmOverloads constructor(
    override val id: Long = 0,
    override val name: String,
    override var uri: Uri? = null,
    override var url: String? = null,
    override var mimeType: String = "",
    var mediaType: Int = 0
) : BaseFile(id, name, uri, url, mimeType)





