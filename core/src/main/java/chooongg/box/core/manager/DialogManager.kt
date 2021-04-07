package chooongg.box.core.manager

import android.app.Dialog
import java.util.*

object DialogManager {

    val queue: Queue<Dialog> = LinkedList()

    var currentDialog: Dialog? = null

    fun showDialog(dialog: Dialog) {
        queue.offer(dialog)
        canShow()
    }

    private fun canShow() {
        if (currentDialog == null) {
            show()
        }
    }

    private fun show() {
        currentDialog = queue.poll()
        if (currentDialog == null) return
        currentDialog!!.show()
        currentDialog!!.setOnDismissListener {

        }
    }
}