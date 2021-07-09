package chooongg.box.picker.activity

import android.os.Bundle
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.ext.dp2px
import chooongg.box.ext.enableElevationOverlay
import chooongg.box.ext.gone
import chooongg.box.ext.visible
import chooongg.box.picker.FilePickerSelectOptions
import chooongg.box.picker.R
import chooongg.box.picker.databinding.ActivityFilePickerSelectMediaBinding
import me.simple.itemdecor.GridItemDecor

class FilePickerSelectMediaActivity : BoxBindingActivity<ActivityFilePickerSelectMediaBinding>() {

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.layoutBottom.enableElevationOverlay()
        binding.tvFolder.setText(
            when {
                FilePickerSelectOptions.onlyShowImages -> R.string.picker_media_all_image
                FilePickerSelectOptions.onlyShowVideos -> R.string.picker_media_all_video
                else -> R.string.picker_media_all_image_video
            }
        )
        if (!FilePickerSelectOptions.isSingle) {
            binding.layoutBottom.visible()
        } else binding.layoutBottom.gone()
        binding.recyclerView.addItemDecoration(GridItemDecor().apply {
            margin = dp2px(4f)
        })
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}