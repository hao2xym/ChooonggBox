package chooongg.box.picker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chooongg.box.core.ext.load
import chooongg.box.ext.gone
import chooongg.box.ext.visible
import chooongg.box.picker.PickerManager
import chooongg.box.picker.R
import chooongg.box.picker.utils.AndroidLifecycleUtils
import chooongg.box.picker.models.PhotoDirectory

class FolderGridAdapter(
    private val context: Context,
    var items: List<PhotoDirectory>,
    private val showCamera: Boolean
) : RecyclerView.Adapter<FolderGridAdapter.PhotoViewHolder>() {

    companion object {
        const val ITEM_TYPE_CAMERA = 100
        const val ITEM_TYPE_PHOTO = 101
    }

    private var folderGridAdapterListener: FolderGridAdapterListener? = null

    interface FolderGridAdapterListener {
        fun onCameraClicked()
        fun onFolderClicked(photoDirectory: PhotoDirectory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val itemView =
            LayoutInflater.from(context).inflate(R.layout.item_picker_folder, parent, false)

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
            val photoDirectory = items[if (showCamera) position - 1 else position]

            if (AndroidLifecycleUtils.canLoadImage(holder.ivPhoto.context)) {
                holder.ivPhoto.load(photoDirectory.getCoverPath())
            }

            holder.tvFolderName.text = photoDirectory.name
            holder.tvFileCount.text = photoDirectory.medias.size.toString()

            holder.itemView.setOnClickListener {
                folderGridAdapterListener?.onFolderClicked(photoDirectory)
            }
            holder.layoutOverlay.visible()
        } else {
            holder.ivPhoto.setImageResource(PickerManager.cameraDrawable)
            holder.itemView.setOnClickListener {
                folderGridAdapterListener?.onCameraClicked()
            }
            holder.layoutOverlay.gone()
        }
    }

    fun setData(newItems: List<PhotoDirectory>) {
        this.items = newItems
    }

    override fun getItemCount(): Int {
        return if (showCamera) items.size + 1 else items.size
    }

    fun setFolderGridAdapterListener(onClickListener: FolderGridAdapterListener) {
        this.folderGridAdapterListener = onClickListener
    }

    class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val ivPhoto: ImageView = itemView.findViewById(R.id.iv_photo)
        internal val tvFolderName: TextView = itemView.findViewById(R.id.tv_folder_name)
        internal val tvFileCount: TextView = itemView.findViewById(R.id.tv_file_count)
        internal val layoutOverlay: View = itemView.findViewById(R.id.layout_overlay)
    }
}
