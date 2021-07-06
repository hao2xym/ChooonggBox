package chooongg.box.picker.models

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
class PickerFileType constructor(
    var title: String,
    var extensions: Array<String>,
    @DrawableRes var drawable: Int
) : Parcelable