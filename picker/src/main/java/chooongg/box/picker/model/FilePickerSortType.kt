package chooongg.box.picker.model

enum class FilePickerSortType(val value: Int) {
    NAME_ASC(0), NAME_DESC(1),            // 按名称
    TIME_ASC(2), TIME_DESC(3),            // 按修改时间
    SIZE_ASC(4), SIZE_DESC(5),            // 按大小
    EXTENSION_ASC(6), EXTENSION_DESC(7)   // 按文件扩展名
}