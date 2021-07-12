package chooongg.box.picker.activity

import android.database.ContentObserver
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import chooongg.box.core.activity.BoxBindingModelActivity
import chooongg.box.core.ext.load
import chooongg.box.core.statePage.state.LoadingState
import chooongg.box.ext.dp2px
import chooongg.box.ext.enableElevationOverlay
import chooongg.box.ext.gone
import chooongg.box.ext.visible
import chooongg.box.log.BoxLog
import chooongg.box.picker.FilePickerSelectOptions
import chooongg.box.picker.R
import chooongg.box.picker.databinding.ActivityFilePickerSelectMediaBinding
import chooongg.box.picker.model.AlbumDirector
import chooongg.box.picker.model.MediaItem
import chooongg.box.picker.utils.AlbumPopupWindowManager
import chooongg.box.picker.viewModel.FilePickerMediaViewModel
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import me.simple.itemdecor.GridItemDecor

class FilePickerSelectMediaActivity :
    BoxBindingModelActivity<ActivityFilePickerSelectMediaBinding, FilePickerMediaViewModel>(),
    FilePickerMediaViewModel.OnMediaListener {

    private val albumPopupWindowManager by lazy {
        AlbumPopupWindowManager(this, binding.btnFolder, binding.tvFolder, binding.ivFolder) {
            model.getMedia(it)
            binding.statePageLayout.show(LoadingState::class)
        }
    }

    private lateinit var contentObserver: ContentObserver

    private val needCamera = FilePickerSelectOptions.needCamera

    private val adapter =
        Adapter(
            needCamera,
            FilePickerSelectOptions.isSingle,
            FilePickerSelectOptions.isNumberSelect
        )

    override fun isShowActionBar() = false

    override fun initConfig(savedInstanceState: Bundle?) {
        model.setOnGetAlbumListener(this)
        binding.layoutBottom.enableElevationOverlay()
        binding.layoutBottom.visibility =
            if (FilePickerSelectOptions.isSingle) View.GONE else View.VISIBLE
        binding.recyclerView.adapter = adapter
        binding.recyclerView.addItemDecoration(GridItemDecor().apply { margin = dp2px(4f) })
    }

    override fun initContent(savedInstanceState: Bundle?) {
        model.getAlbum()
        contentObserver = object : ContentObserver(null) {
            override fun onChange(selfChange: Boolean) {
                model.getAlbum()
            }
        }
        contentResolver.registerContentObserver(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver
        )
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
        if (data.isEmpty()) {

        } else {
            binding.statePageLayout.showSuccess()
        }
        adapter.setNewInstance(data)
    }

    override fun onMediaLoadError(e: Exception) {
        BoxLog.e(e)
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(contentObserver)
        albumPopupWindowManager.destroy()
    }

    private data class Item(val isCamera: Boolean, val media: MediaItem)

    private class Adapter(
        val needCamera: Boolean,
        val isSingle: Boolean,
        val isNumberSelected: Boolean
    ) : BaseQuickAdapter<Item, BaseViewHolder>(R.layout.item_picker_media) {

        private val selectedIds = ArrayList<Long>()

        override fun convert(holder: BaseViewHolder, item: Item) {
            if (item.isCamera) {
                holder.setImageResource(R.id.iv_photo, R.drawable.layer_camera)
                    .setGone(R.id.view_mask, true)
                    .setGone(R.id.iv_play, true)
                    .setGone(R.id.checkbox, true)
            } else {
                holder.getView<AppCompatImageView>(R.id.iv_photo).load(item.media.path)
                val checkbox = holder.getView<TextView>(R.id.checkbox)
                if (isSingle) {
                    checkbox.gone()
                    checkbox.setOnClickListener(null)
                } else {
                    checkbox.visible()
                    if (selectedIds.contains(item.media.id)) {
                        if (isNumberSelected) {
                            checkbox.text = selectedIds.indexOf(item.media.id).toString()
                            checkbox.setBackgroundResource(R.drawable.shape_picker_check_checked)
                        } else {
                            checkbox.setBackgroundResource(R.drawable.layer_picker_check_checked)
                        }
                    } else {
                        checkbox.setBackgroundResource(R.drawable.shape_picker_check)
                    }
                    checkbox.setOnClickListener {
                        if (selectedIds.contains(item.media.id)) {
                            selectedIds.remove(item.media.id)
                            checkbox.setBackgroundResource(R.drawable.shape_picker_check)
                        } else {
                            selectedIds.add(item.media.id)
                            data.forEachIndexed { index, temp ->
                                if (selectedIds.contains(item.media.id)) {
                                    notifyItemChanged(if (needCamera) index + 1 else index)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}