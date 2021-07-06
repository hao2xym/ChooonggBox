package chooongg.box.picker.adapters

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import chooongg.box.core.ext.load
import chooongg.box.core.widget.SmoothCheckBox
import chooongg.box.ext.gone
import chooongg.box.ext.visible
import chooongg.box.picker.FilePickerConst
import chooongg.box.picker.PickerManager
import chooongg.box.picker.R
import chooongg.box.picker.utils.AndroidLifecycleUtils
import chooongg.box.picker.models.Media

class PhotoGridAdapter(
    private val context: Context,
    medias: List<Media>,
    selectedPaths: MutableList<Uri>,
    private val showCamera: Boolean,
    private val mListener: FileAdapterListener?
) : SelectableAdapter<PhotoGridAdapter.PhotoViewHolder, Media>(medias, selectedPaths) {

    companion object {
        const val ITEM_TYPE_CAMERA = 100
        const val ITEM_TYPE_PHOTO = 101
    }

    private var cameraOnClickListener: View.OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_picker_photo, parent, false)

        return PhotoViewHolder(itemView)
    }

    override fun getItemViewType(position: Int): Int {
        return if (showCamera)
            if (position == 0) ITEM_TYPE_CAMERA else ITEM_TYPE_PHOTO
        else
            ITEM_TYPE_PHOTO
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        if (getItemViewType(position) == ITEM_TYPE_PHOTO) {

            val media = items[if (showCamera) position - 1 else position]

            if (AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.context)) {
                holder.ivPhoto.load(media.path)
            }


            if (media.mediaType == FilePickerConst.MEDIA_TYPE_VIDEO) holder.ivPlay.visible()
            else holder.ivPlay.gone()

            holder.itemView.setOnClickListener { onItemClicked(holder, media) }

            //in some cases, it will prevent unwanted situations
            holder.checkBox.gone()
            holder.checkBox.setOnCheckedChangeListener(null)
            holder.checkBox.setOnClickListener { onItemClicked(holder, media) }

            //if true, your checkbox will be selected, else unselected
            holder.checkBox.isChecked = isSelected(media)

            holder.viewMask.visibility = if (isSelected(media)) View.VISIBLE else View.GONE
            holder.checkBox.visibility = if (isSelected(media)) View.VISIBLE else View.GONE

            holder.checkBox.setOnCheckedChangeListener(object :
                SmoothCheckBox.OnCheckedChangeListener {
                override fun onCheckedChanged(checkBox: SmoothCheckBox, isChecked: Boolean) {
                    toggleSelection(media)
                    holder.viewMask.visibility = if (isChecked) View.VISIBLE else View.GONE

                    if (isChecked) {
                        holder.checkBox.visible()
                        PickerManager.add(media.path, FilePickerConst.FILE_TYPE_MEDIA)
                    } else {
                        holder.checkBox.gone()
                        PickerManager.remove(media.path, FilePickerConst.FILE_TYPE_MEDIA)
                    }

                    mListener?.onItemSelected()
                }
            })

        } else {
            holder.ivPhoto.setImageResource(PickerManager.cameraDrawable)
            holder.checkBox.visibility = View.GONE
            holder.itemView.setOnClickListener(cameraOnClickListener)
            holder.ivPlay.visibility = View.GONE
        }
    }

    private fun onItemClicked(holder: PhotoViewHolder, media: Media) {
        if (PickerManager.getMaxCount() == 1) {
            PickerManager.add(media.path, FilePickerConst.FILE_TYPE_MEDIA)
            mListener?.onItemSelected()
        } else if (holder.checkBox.isChecked || PickerManager.shouldAdd()) {
            holder.checkBox.setChecked(!holder.checkBox.isChecked, true)
        }
    }

    override fun getItemCount(): Int {
        return if (showCamera) items.size + 1 else items.size
    }

    fun setCameraListener(onClickListener: View.OnClickListener) {
        this.cameraOnClickListener = onClickListener
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var checkBox: SmoothCheckBox = itemView.findViewById(R.id.checkbox)
        var ivPhoto: ImageView = itemView.findViewById(R.id.iv_photo)
        var viewMask: View = itemView.findViewById(R.id.view_mask)
        var ivPlay: ImageView = itemView.findViewById(R.id.iv_play)
    }
}
