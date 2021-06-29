//package chooongg.box.simple.modules.main.entity
//
//import chooongg.box.core.adapter.BindingItem
//import chooongg.box.simple.databinding.ItemMainBinding
//
//data class MainItemEntity(val name: CharSequence) : BindingItem<ItemMainBinding>() {
//    override val type: Int
//        get() = 0
//
//    override fun bindView(binding: ItemMainBinding, payloads: List<Any>) {
//        binding.tvName.text = name
//    }
//}