package chooongg.box.picker

import android.app.Activity
import androidx.fragment.app.Fragment
import java.lang.ref.WeakReference

class FilePicker private constructor(activity: Activity?, fragment: Fragment?) {

    private val activity: WeakReference<Activity> = WeakReference(activity)
    private val fragment: WeakReference<Fragment> = WeakReference(fragment)

    companion object {
        fun from(activity: Activity) = FilePicker(activity, null)
        fun from(fragment: Fragment) = FilePicker(fragment.activity, fragment)
    }

    internal fun getActivity() = activity.get()
    internal fun getFragment() = fragment.get()

    fun chooseFile() = FilePickerSelectFileCreator(this)
    fun chooseMedia() = FilePickerSelectMediaCreator(this)
}