package chooongg.box.simple.modules.main.adapter

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import chooongg.box.ext.doOnClick
import chooongg.box.ext.layoutInflater
import chooongg.box.simple.databinding.ItemMainBinding
import chooongg.box.simple.modules.main.entity.MainItemEntity

internal class MainAdapter(private var data: List<MainItemEntity>) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private var onClickListener: View.OnClickListener? = null

    class ViewHolder(val binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemMainBinding.inflate(parent.context.layoutInflater, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = data[position].name
        holder.binding.ivImage.setImageResource(data[position].imgRes)
        holder.itemView.doOnClick {
            onClickListener?.onClick(it)
        }
    }

    override fun getItemCount() = data.size

    fun setOnClickListener(onClickListener: View.OnClickListener) {
        this.onClickListener = onClickListener
    }
}