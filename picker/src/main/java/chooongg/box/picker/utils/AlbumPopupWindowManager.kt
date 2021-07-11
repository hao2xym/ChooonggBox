package chooongg.box.picker.utils

import android.app.Activity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.ListPopupWindow
import androidx.core.view.updateLayoutParams
import chooongg.box.core.ext.loadDefault
import chooongg.box.ext.*
import chooongg.box.picker.R
import chooongg.box.picker.databinding.ItemPickerAlbumBinding
import chooongg.box.picker.model.AlbumDirector

class AlbumPopupWindowManager(
    private val context: Activity,
    private val btnView: View,
    private val textView: TextView,
    private val arrowImage: ImageView
) {

    private val adapter = Adapter()

    private var data = ArrayList<AlbumDirector>()

    private val listPopupWindow by lazy {
        ListPopupWindow(context, null, R.attr.listPopupWindowStyle).apply {
            isModal = true
            anchorView = btnView
            setDropDownGravity(Gravity.END)
            setContentWidth(context.getScreenWidth() / 2)
            setAdapter(adapter)
            setOnDismissListener {
                arrowImage.animate().rotation(0f)
            }
            btnView.setOnTouchListener(createDragToOpenListener(btnView))
        }
    }

    fun setData(data: ArrayList<AlbumDirector>?) {
        if (data.isNullOrEmpty()) {
            btnView.gone()
        } else {
            btnView.visible()
            this.data = data
            adapter.notifyDataSetChanged()
            if (textView.text.isNullOrEmpty()) {
                textView.text = data[0].name
            } else {
                var isEquals = false
                for (i in 0 until data.size) {
                    if (textView.text == data[i].name) {
                        isEquals = true
                        break
                    }
                }
                if (!isEquals) {
                    textView.text = data[0].name
                }
            }
        }
    }

    init {
        btnView.updateLayoutParams<ViewGroup.LayoutParams> {
            width = context.getScreenWidth() / 2
        }
        btnView.doOnClick {
            arrowImage.animate().rotation(180f)
            val itemHeight = dp2px(72f)
            listPopupWindow.height =
                (if (data.size > 6) 6 * itemHeight else data.size * itemHeight) + dp2px(16f)
            listPopupWindow.show()
        }
    }

    fun destroy() {
        listPopupWindow.dismiss()
    }

    private inner class Adapter : BaseAdapter() {
        override fun getCount() = data.size
        override fun getItem(position: Int) = data[position]
        override fun getItemId(position: Int) = position.toLong()
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val holder: ViewHolder
            val view: View
            if (convertView == null) {
                holder = ViewHolder(
                    ItemPickerAlbumBinding.inflate(
                        context.layoutInflater,
                        parent,
                        false
                    )
                )
                view = holder.binding.root
                view.tag = holder
            } else {
                view = convertView
                holder = view.tag as ViewHolder
            }
            holder.binding.ivPhoto.loadDefault(data[position].coverUri)
            holder.binding.tvFolderName.text = data[position].name
            holder.binding.tvFileCount.text = data[position].count.toString()
            return view
        }

        private inner class ViewHolder(val binding: ItemPickerAlbumBinding)
    }
}