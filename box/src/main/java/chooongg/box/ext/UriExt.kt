package chooongg.box.ext

import android.net.Uri
import java.util.*

fun Uri?.getMimeType() = if (this != null) {
    APP.contentResolver.getType(this)?.lowercase(Locale.getDefault())
} else null