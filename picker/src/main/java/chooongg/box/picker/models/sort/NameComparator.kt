package chooongg.box.picker.models.sort

import chooongg.box.picker.models.Document
import java.util.*

class NameComparator : Comparator<Document> {
    override fun compare(o1: Document, o2: Document) =
        o1.name.lowercase(Locale.getDefault()).compareTo(o2.name.lowercase(Locale.getDefault()))
}