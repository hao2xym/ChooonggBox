package chooongg.box.picker.fragment

import android.os.Bundle
import android.os.Environment
import chooongg.box.core.fragment.BoxBindingFragment
import chooongg.box.picker.databinding.FragmentFilePickerFileBrowserBinding
import java.io.File

class FilePickerSelectFileBrowserFragment :
    BoxBindingFragment<FragmentFilePickerFileBrowserBinding>() {

    /*当前目录，默认是SD卡根目录*/

    private var currentFolder =
        Environment.getExternalStorageDirectory().absolutePath + File.separator

    /*所有可访问存储设备列表*/
//    private val mSdCardList: List<String> = FileUtils.getAllSdPaths(this);

    override fun initContentByLazy() {
    }

    override fun initConfig(savedInstanceState: Bundle?) {
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }

}