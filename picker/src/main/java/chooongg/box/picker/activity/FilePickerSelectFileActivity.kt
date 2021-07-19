package chooongg.box.picker.activity

import android.os.Bundle
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.core.fragment.BoxFragment
import chooongg.box.picker.databinding.ActivityFilePickerSelectFileBinding
import chooongg.box.picker.fragment.FilePickerSelectFileBrowserFragment
import chooongg.box.picker.fragment.FilePickerSelectFileCommonlyFragment

class FilePickerSelectFileActivity : BoxBindingActivity<ActivityFilePickerSelectFileBinding>() {

    private val fragments by lazy {
        arrayListOf<BoxFragment>(
            FilePickerSelectFileCommonlyFragment(),
            FilePickerSelectFileBrowserFragment()
        )
    }

    override fun initConfig(savedInstanceState: Bundle?) {

    }

    override fun initContent(savedInstanceState: Bundle?) {

    }
}