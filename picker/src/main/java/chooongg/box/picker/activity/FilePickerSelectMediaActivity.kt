package chooongg.box.picker.activity

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.ext.dp2px
import chooongg.box.ext.enableElevationOverlay
import chooongg.box.ext.gone
import chooongg.box.ext.visible
import chooongg.box.picker.FilePickerSelectOptions
import chooongg.box.picker.databinding.ActivityFilePickerSelectMediaBinding
import chooongg.box.picker.model.AlbumDirector
import chooongg.box.picker.utils.AlbumPopupWindowManager
import chooongg.box.picker.viewModel.FilePickerMediaViewModel
import me.simple.itemdecor.GridItemDecor

class FilePickerSelectMediaActivity : BoxBindingActivity<ActivityFilePickerSelectMediaBinding>(),
    FilePickerMediaViewModel.OnMediaListener {

    private val albumViewModel by lazy {
        ViewModelProvider(this).get(FilePickerMediaViewModel::class.java).apply {
            setOnGetAlbumListener(this@FilePickerSelectMediaActivity)
        }
    }

    private val albumPopupWindowManager by lazy {
        AlbumPopupWindowManager(this, binding.btnFolder, binding.tvFolder, binding.ivFolder)
    }

    private var selectBucketId: Long? = null

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.layoutBottom.enableElevationOverlay()
        if (!FilePickerSelectOptions.isSingle) {
            binding.layoutBottom.visible()
        } else binding.layoutBottom.gone()
        binding.recyclerView.addItemDecoration(GridItemDecor().apply {
            margin = dp2px(4f)
        })
    }

    override fun initContent(savedInstanceState: Bundle?) {

    }

    private fun getAlbum(){
        albumViewModel.getAlbum()
    }

    override fun onAlbumLoaded(albums: ArrayList<AlbumDirector>) {
        albumPopupWindowManager.setData(albums)
    }

    override fun onAlbumError(e: Exception) {
    }

    override fun onDestroy() {
        super.onDestroy()
        albumPopupWindowManager.destroy()
    }
}