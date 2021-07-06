package chooongg.box.picker.models.sort

import chooongg.box.picker.models.Document
import java.util.*

enum class SortingTypes(val comparator: Comparator<Document>?) {
    NAME(NameComparator()), NONE(null);
}