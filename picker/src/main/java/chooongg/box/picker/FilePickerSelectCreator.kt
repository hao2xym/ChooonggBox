package chooongg.box.picker

abstract class FilePickerSelectCreator internal constructor(
    val filePicker: FilePicker
) {

    init {
        FilePickerSelectOptions.reset()
    }

}