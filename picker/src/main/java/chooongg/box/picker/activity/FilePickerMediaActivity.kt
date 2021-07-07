package chooongg.box.picker.activity

import android.os.Bundle
import android.provider.MediaStore
import chooongg.box.core.activity.BoxBindingModelActivity
import chooongg.box.ext.dp2px
import chooongg.box.picker.FilePickerManager
import chooongg.box.picker.adapter.MediaGridAdapter
import chooongg.box.picker.databinding.ActivityPickerMediaBinding
import chooongg.box.picker.viewmodels.MediaPickerViewModel
import me.simple.itemdecor.GridItemDecor

class FilePickerMediaActivity :
    BoxBindingModelActivity<ActivityPickerMediaBinding, MediaPickerViewModel>() {

    private val adapter = MediaGridAdapter(this, FilePickerManager.isEnableCamera) {

    }

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(GridItemDecor().apply {
            margin = dp2px(4f)
        })
        model.lvMediaData.observe(this) {
            adapter.setNewData(it)
            binding.statePageLayout.showSuccess()
        }
    }

    override fun initContent(savedInstanceState: Bundle?) {
        model.getMedia(null, MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE)
    }
}