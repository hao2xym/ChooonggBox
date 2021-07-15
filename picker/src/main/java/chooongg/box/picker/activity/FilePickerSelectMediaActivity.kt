package chooongg.box.picker.activity

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import chooongg.box.core.activity.BoxBindingModelActivity
import chooongg.box.core.adapter.BindingAdapter
import chooongg.box.core.adapter.BindingHolder
import chooongg.box.core.ext.load
import chooongg.box.core.statePage.state.EmptyState
import chooongg.box.core.statePage.state.ErrorState
import chooongg.box.core.statePage.state.LoadingState
import chooongg.box.ext.*
import chooongg.box.picker.FilePickerSelectOptions
import chooongg.box.picker.R
import chooongg.box.picker.databinding.ActivityFilePickerSelectMediaBinding
import chooongg.box.picker.databinding.ItemPickerMediaBinding
import chooongg.box.picker.model.AlbumDirector
import chooongg.box.picker.model.MediaItem
import chooongg.box.picker.utils.AlbumPopupWindowManager
import chooongg.box.picker.viewModel.FilePickerMediaViewModel
import com.google.android.material.snackbar.Snackbar
import me.simple.itemdecor.GridItemDecor

class FilePickerSelectMediaActivity :
    BoxBindingModelActivity<ActivityFilePickerSelectMediaBinding, FilePickerMediaViewModel>(),
    FilePickerMediaViewModel.OnMediaListener {

    private val albumPopupWindowManager by lazy {
        AlbumPopupWindowManager(this, binding.btnFolder, binding.tvFolder, binding.ivFolder) {
            binding.statePageLayout.show(LoadingState::class)
            model.getMedia(it)
        }
    }

    private val needCamera = FilePickerSelectOptions.needCamera

    private val adapter = Adapter(
        FilePickerSelectOptions.isSingle,
        FilePickerSelectOptions.isNumberSelect
    )

    private lateinit var takePicture: ActivityResultLauncher<Uri>
    private lateinit var takeVideo: ActivityResultLauncher<Uri>

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        model.setOnGetAlbumListener(this)
        binding.layoutBottom.enableElevationOverlay()
        binding.layoutBottom.visibility =
            if (FilePickerSelectOptions.isSingle) View.GONE else View.VISIBLE
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = null
        binding.recyclerView.addItemDecoration(GridItemDecor().apply { margin = dp2px(4f) })
        adapter.setOnItemChildClickListener { _, _, position ->
            if (FilePickerSelectOptions.selectedMedia.contains(adapter.data[position].media)) {
                FilePickerSelectOptions.selectedMedia.remove(adapter.data[position].media)
                adapter.data.forEachIndexed { index, item ->
                    if (FilePickerSelectOptions.selectedMedia.contains(item.media)) {
                        adapter.notifyItemChanged(index + adapter.headerLayoutCount)
                    }
                }
                adapter.notifyItemChanged(position + adapter.headerLayoutCount)
            } else {
                if (FilePickerSelectOptions.selectedMedia.size < FilePickerSelectOptions.maxCount) {
                    FilePickerSelectOptions.selectedMedia.add(adapter.data[position].media)
                    adapter.notifyItemChanged(position + adapter.headerLayoutCount)
                } else {
                    Snackbar.make(
                        binding.statePageLayout,
                        "最多只能选择${FilePickerSelectOptions.maxCount}张",
                        Snackbar.LENGTH_SHORT
                    ).apply {
                        anchorView = binding.layoutBottom
                    }.show()
                }
            }
            if (FilePickerSelectOptions.selectedMedia.isEmpty()) {
                binding.btnDone.text = resourcesString(R.string.picker_done)
            } else {
                binding.btnDone.text = resourcesString(
                    R.string.picker_done_multiple,
                    FilePickerSelectOptions.selectedMedia.size,
                    FilePickerSelectOptions.maxCount
                )
            }
        }
        takePicture = registerForActivityResult(ActivityResultContracts.TakePicture()) {

        }
        takeVideo = registerForActivityResult(ActivityResultContracts.TakeVideo()) {

        }
        adapter.setOnItemClickListener { _, _, position ->
            if (adapter.data[position].isCamera) {
//                when {
//                    FilePickerSelectOptions.onlyShowImages -> takePicture.launch()
//                    FilePickerSelectOptions.onlyShowVideos -> takeVideo.launch()
//                    else -> {
//
//                    }
//                }
            } else {
                // TODO 图片预览
            }
        }
    }

    override fun initContent(savedInstanceState: Bundle?) {
        model.getAlbum()
        model.registerContentObserver()
    }

    override fun onAlbumLoaded(albums: ArrayList<AlbumDirector>) {
        albumPopupWindowManager.setData(albums)
        model.getMedia(albumPopupWindowManager.getSelectBucketId())
        binding.statePageLayout.show(LoadingState::class)
    }

    override fun onMediaLoaded(mediaData: ArrayList<MediaItem>) {
        val data = ArrayList<Item>()
        mediaData.forEach { data.add(Item(false, it)) }
        if (needCamera) {
            if (albumPopupWindowManager.getSelectBucketId() == null && FilePickerSelectOptions.needCamera) {
                data.add(0, Item(true, MediaItem(-1, null, null, null, false, 0)))
            }
        }
        adapter.setNewInstance(data)
        if (data.isEmpty()) {
            binding.statePageLayout.show(EmptyState::class)
        } else {
            binding.statePageLayout.showSuccess()
        }
    }

    override fun onMediaLoadError(e: Exception) {
        binding.statePageLayout.show(ErrorState::class, "无法查找图片")
    }

    override fun onDestroy() {
        super.onDestroy()
        albumPopupWindowManager.destroy()
    }

    private data class Item(val isCamera: Boolean, val media: MediaItem)

    private class Adapter(
        val isSingle: Boolean,
        val isNumberSelected: Boolean
    ) : BindingAdapter<Item, ItemPickerMediaBinding>() {

        init {
            addChildClickViewIds(R.id.btn_checkbox)
        }

        override fun convert(holder: BindingHolder<ItemPickerMediaBinding>, item: Item) {
            if (item.isCamera) {
                holder.binding.ivPhoto.setImageResource(R.drawable.layer_camera)
                holder.binding.viewMask.gone()
                holder.binding.ivPlay.gone()
                holder.binding.btnCheckbox.gone()
            } else {
                holder.binding.ivPhoto.load(item.media.uri)
                holder.binding.ivPlay.visibility =
                    if (item.media.isVideo) View.VISIBLE else View.GONE
                holder.binding.viewMaskVideo.visibility =
                    if (item.media.isVideo) View.VISIBLE else View.GONE
                if (isSingle) {
                    holder.binding.btnCheckbox.gone()
                } else {
                    holder.binding.btnCheckbox.visible()
                    if (!FilePickerSelectOptions.selectedMedia.contains(item.media)) {
                        holder.binding.checkbox.text = null
                        holder.binding.checkbox.setBackgroundResource(R.drawable.shape_picker_check)
                        holder.binding.viewMask.animate().alpha(0f)
                    } else if (isNumberSelected) {
                        val index = FilePickerSelectOptions.selectedMedia.indexOf(item.media)
                        holder.binding.checkbox.text = (index + 1).toString()
                        holder.binding.checkbox.setBackgroundResource(R.drawable.shape_picker_check_number)
                        holder.binding.viewMask.animate().alpha(1f)
                    } else {
                        holder.binding.checkbox.text = null
                        holder.binding.checkbox.setBackgroundResource(R.drawable.layer_picker_check_checked)
                        holder.binding.viewMask.animate().alpha(1f)
                    }
                }
            }
        }
    }
}