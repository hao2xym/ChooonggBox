package chooongg.box.picker.fragment

import android.os.Bundle
import android.os.Environment
import android.os.FileUtils
import androidx.core.content.ContextCompat
import chooongg.box.core.fragment.BoxBindingFragment
import chooongg.box.picker.databinding.FragmentFilePickerBroadBinding
import java.io.File

class FilePickerSelectFileBFragment : BoxBindingFragment<FragmentFilePickerBroadBinding>() {

    /*当前目录，默认是SD卡根目录*/
    private var currentFolder = Environment.getExternalStorageDirectory().absolutePath + File.separator

    /*所有可访问存储设备列表*/
//    private val mSdCardList: List<String> = FileUtils.getAllSdPaths(this);

    override fun initContentByLazy() {
        ContextCompat.getExternalFilesDirs(requireContext(),null).forEach {
            it.absoluteFile
        }
    }

    override fun initConfig(savedInstanceState: Bundle?) {
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }

}