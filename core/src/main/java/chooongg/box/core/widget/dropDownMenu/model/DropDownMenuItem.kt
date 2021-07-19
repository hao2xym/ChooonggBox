package chooongg.box.core.widget.dropDownMenu.model

data class DropDownMenuItem(
    val id: Int,
    val title: CharSequence,
    val isShowArrow: Boolean = false
) : Comparable<DropDownMenuItem> {
    override fun compareTo(other: DropDownMenuItem): Int {
        return id - other.id
    }
}
