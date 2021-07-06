package chooongg.box.picker.models

import android.net.Uri
import kotlinx.parcelize.Parcelize

@Parcelize
class Media @JvmOverloads constructor(
    override var id: Long = 0,
    override var name: String,
    override var path: Uri,
    var mediaType: Int = 0
) : BaseFile(id, name, path)





