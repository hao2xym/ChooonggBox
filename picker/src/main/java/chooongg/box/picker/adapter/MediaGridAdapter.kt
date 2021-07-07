package chooongg.box.picker.adapter

import android.content.Context
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chooongg.box.core.ext.loadDefault
import chooongg.box.ext.gone
import chooongg.box.ext.visible
import chooongg.box.picker.FilePickerManager
import chooongg.box.picker.R
import chooongg.box.picker.databinding.ItemPickerPhotoBinding
import chooongg.box.picker.models.Media

class MediaGridAdapter(
    private val context: Context,
    private val isShowCamera: Boolean,
    private val onItemSelectedListener: () -> Unit
) : RecyclerView.Adapter<MediaGridAdapter.MediaViewHolder>() {

    companion object {
        const val ITEM_TYPE_CAMERA = 100
        const val ITEM_TYPE_PHOTO = 101
    }

    private var data: List<Media> = ArrayList()

    private var cameraOnClickListener: View.OnClickListener? = null

    fun setNewData(data: List<Media>) {
        this.data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MediaViewHolder(
        ItemPickerPhotoBinding.inflate(LayoutInflater.from(context), parent, false)
    )

    override fun getItemViewType(position: Int): Int {
        return if (isShowCamera)
            if (position == 0) ITEM_TYPE_CAMERA else ITEM_TYPE_PHOTO
        else
            ITEM_TYPE_PHOTO
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {
            val media = data[if (isShowCamera) position - 1 else position]
            holder.binding.ivPhoto.loadDefault(media.uri)

            if (media.mediaType == MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO) holder.binding.ivPlay.visible()
            else holder.binding.ivPlay.gone()

            holder.itemView.setOnClickListener { onItemClicked(holder, media) }

            if (FilePickerManager.maxCount != 1) {
                holder.binding.checkbox.visible()
                if (media.isSelected) {
                    holder.binding.checkbox.setImageResource(R.drawable.ic_picker_check_checked)
                } else {
                    holder.binding.checkbox.setImageResource(R.drawable.ic_picker_check)
                }
            } else holder.binding.checkbox.gone()

            holder.binding.viewMask.alpha = if (media.isSelected) 1f else 0f
            holder.binding.checkbox.setOnClickListener {
                if (!media.isSelected) {
                    if (FilePickerManager.shouldAdd()) {
                        FilePickerManager.add(media.uri, true)
                        media.isSelected = true
                        onItemSelectedListener()
                    }
                } else {
                    FilePickerManager.remove(media.uri, true)
                    media.isSelected = false
                    onItemSelectedListener()
                }
                holder.binding.viewMask.animate().alpha(if (media.isSelected) 1f else 0f)
                if (media.isSelected) {
                    holder.binding.checkbox.setImageResource(R.drawable.ic_picker_check_checked)
                } else {
                    holder.binding.checkbox.setImageResource(R.drawable.ic_picker_check)
                }
            }
        } else {
            holder.itemView.setOnClickListener(cameraOnClickListener)
            holder.binding.ivPhoto.setImageResource(FilePickerManager.cameraIcon)
            holder.binding.checkbox.gone()
            holder.binding.ivPlay.gone()
            holder.binding.viewMask.alpha = 0f
        }
    }

    private fun onItemClicked(holder: MediaViewHolder, media: Media) {
        if (FilePickerManager.maxCount == 1) {
            FilePickerManager.add(media.uri, true)
            onItemSelectedListener()
        } else {
            // 预览
        }
    }

    override fun getItemCount(): Int {
        return if (isShowCamera) data.size + 1 else data.size
    }

    fun setCameraListener(onClickListener: View.OnClickListener) {
        this.cameraOnClickListener = onClickListener
    }

    class MediaViewHolder(val binding: ItemPickerPhotoBinding) :
        RecyclerView.ViewHolder(binding.root)
}
