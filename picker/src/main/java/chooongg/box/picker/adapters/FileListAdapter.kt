package chooongg.box.picker.adapters

import android.content.Context
import android.net.Uri
import android.text.format.Formatter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import chooongg.box.core.widget.SmoothCheckBox
import chooongg.box.picker.FilePickerConst
import chooongg.box.picker.PickerManager
import chooongg.box.picker.R
import chooongg.box.picker.models.Document
import java.util.*

/**
 * Created by droidNinja on 29/07/16.
 */
class FileListAdapter(
    private val context: Context,
    private var mFilteredList: List<Document>,
    selectedPaths: MutableList<Uri>,
    private val mListener: FileAdapterListener?
) : SelectableAdapter<FileListAdapter.FileViewHolder, Document>(mFilteredList, selectedPaths),
    Filterable {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_picker_doc, parent, false)

        return FileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val document = mFilteredList[position]

        val drawable = document.fileType?.drawable ?: R.drawable.ic_picker_other
        holder.ivFile.setImageResource(drawable)

        holder.tvFileName.text = document.name
        holder.tvFileSize.text = Formatter.formatShortFileSize(
            context, java.lang.Long.parseLong(document.size ?: "0")
        )

        holder.itemView.setOnClickListener { onItemClicked(document, holder) }

        //in some cases, it will prevent unwanted situations
        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.setOnClickListener { onItemClicked(document, holder) }

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.isChecked = isSelected(document)

        holder.itemView.isSelected = isSelected(document)
        holder.checkBox.visibility = if (isSelected(document)) View.VISIBLE else View.GONE

        holder.checkBox.setOnCheckedChangeListener(object : SmoothCheckBox.OnCheckedChangeListener {
            override fun onCheckedChanged(checkBox: SmoothCheckBox, isChecked: Boolean) {
                toggleSelection(document)
                if (isChecked) {
                    PickerManager.add(document.path, FilePickerConst.FILE_TYPE_DOCUMENT)
                } else {
                    PickerManager.remove(document.path, FilePickerConst.FILE_TYPE_DOCUMENT)
                }
                holder.itemView.isSelected = isChecked
            }
        })
    }

    private fun onItemClicked(document: Document, holder: FileViewHolder) {
        if (PickerManager.getMaxCount() == 1) {
            PickerManager.add(document.path, FilePickerConst.FILE_TYPE_DOCUMENT)
        } else {
            if (holder.checkBox.isChecked) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked, true)
                holder.checkBox.visibility = View.GONE
            } else if (PickerManager.shouldAdd()) {
                holder.checkBox.setChecked(!holder.checkBox.isChecked, true)
                holder.checkBox.visibility = View.VISIBLE
            }
        }

        mListener?.onItemSelected()
    }

    override fun getItemCount(): Int {
        return mFilteredList.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                mFilteredList = if (charString.isEmpty()) items else {
                    val filteredList = ArrayList<Document>()
                    for (document in items) {
                        if (document.name.lowercase(Locale.getDefault()).contains(charString)) {
                            filteredList.add(document)
                        }
                    }
                    filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = mFilteredList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                mFilteredList = filterResults.values as List<Document>
                notifyDataSetChanged()
            }
        }
    }

    class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val checkBox: SmoothCheckBox = itemView.findViewById(R.id.checkbox)
        internal val ivFile: ImageView = itemView.findViewById(R.id.iv_file)
        internal val tvFileName: TextView = itemView.findViewById(R.id.tv_file_name)
        internal val tvFileSize: TextView = itemView.findViewById(R.id.tv_file_size)
    }
}
