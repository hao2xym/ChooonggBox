package chooongg.box.picker.models

import android.net.Uri
import kotlinx.parcelize.Parcelize

@Parcelize
class Document constructor(
    override val id: Long,
    override val name: String,
    override var uri: Uri? = null,
    override var url: String? = null,
    override var mimeType: String = "",
    var size: String? = null,
    var fileType: PickerFileType? = null
) : BaseFile(id, name, uri, url, mimeType)