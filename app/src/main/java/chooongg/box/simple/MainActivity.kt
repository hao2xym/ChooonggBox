package chooongg.box.simple

import android.os.Bundle
import android.view.ViewGroup
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import chooongg.box.core.activity.BoxViewBindingActivity
import chooongg.box.ext.layoutInflater
import chooongg.box.simple.databinding.ActivityMainBinding
import chooongg.box.simple.databinding.ItemMainBinding

class MainActivity : BoxViewBindingActivity<ActivityMainBinding>() {

    private val modules by lazy { arrayListOf("") }
    private val adapter by lazy { Adapter(modules) }

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }

    class Adapter(private var data: List<String>) : RecyclerView.Adapter<Adapter.ViewHolder>() {

        class ViewHolder(binding: ItemMainBinding) : RecyclerView.ViewHolder(binding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemMainBinding.inflate(parent.context.layoutInflater, parent, false)
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.updateLayoutParams<RecyclerView.LayoutParams> {
                height = width
            }
        }

        override fun getItemCount() = data.size
    }
}