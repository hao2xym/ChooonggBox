package chooongg.box.simple.modules.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import chooongg.box.core.activity.BoxViewBindingActivity
import chooongg.box.core.ext.setNightMode
import chooongg.box.log.BoxLog
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityMainBinding
import chooongg.box.simple.modules.main.adapter.MainAdapter
import chooongg.box.simple.modules.main.entity.MainItemEntity

class MainActivity : BoxViewBindingActivity<ActivityMainBinding>() {

    private val modules = arrayListOf(
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar)
    )
    private val adapter = MainAdapter(modules)


    override fun initConfig(savedInstanceState: Bundle?) {
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        supportActionBar?.subtitle = "chooongg@outlook.com"
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter.apply {
            setOnClickListener {
                BoxLog.e(
                    supportActionBar?.isShowing,
                    "{\"Authorization\":\"eyJ0eXAiOiJKV1QiLCJub25jZSI6InQzdURWOEY3dTcwS0QzaTF5eVJOQW45Q3hHLUJyb3otQXk0aUFXN3kyWnciLCJhbGciOiJSUzI1NiIsIng1dCI6IkpSNGxDdzkwTVAzZGhldWptdzdJNVNVUWE5NCIsImtpZCI6IkpSNGxDdzkwTVAzZGhldWptdzdJNVNVUWE5NCJ9.eyJhdWQiOiJodHRwczovL21pY3Jvc29mdGdyYXBoLmNoaW5hY2xvdWRhcGkuY24iLCJpc3MiOiJodHRwczovL3N0cy5jaGluYWNsb3VkYXBpLmNuLzAzMjU4ZjcyLWVlMzctNGJiYi1iMzhkLTZlZmRmYzk2Y2RlNi8iLCJpYXQiOjE2MTQ3MzY1ODgsIm5iZiI6MTYxNDczNjU4OCwiZXhwIjoxNjE0NzQwNDg4LCJhY3IiOiIxIiwiYWlvIjoiNDJKZ1lOamVucnFyV3Uyc1F2M0M5NFZPL3h0elJSVmVsbjMvc0k3bncwcTl6dWo4bml3QSIsImFtciI6WyJwd2QiXSwiYXBwX2Rpc3BsYXluYW1lIjoiQ01BLVNURy1MT0NBTDAxIiwiYXBwaWQiOiIxZTQyZmRmNi05ZGZmLTQ2ZTMtYTcxOC1hODhkNTU3ZGU0NjEiLCJhcHBpZGFjciI6IjAiLCJpcGFkZHIiOiI0MC4xMjUuMjA2LjEzMiIsIm5hbWUiOiJXSFRaWjIiLCJvaWQiOiJiNGEwMjQzMS03ZjNlLTQxODgtYTllMy1iMTQ3YjM2MzNiYzgiLCJwbGF0ZiI6IjEiLCJwdWlkIjoiMTAwMzMyMzBDNUFBMkEyNCIsInB3ZF9leHAiOiI0NDUwODkiLCJwd2RfdXJsIjoiaHR0cHM6Ly9hY2NvdW50LmFjdGl2ZWRpcmVjdG9yeS53aW5kb3dzYXp1cmUuY24vQ2hhbmdlUGFzc3dvcmQuYXNweCIsInJoIjoiMC5BQUFBY284bEF6ZnV1MHV6alc3OV9KYk41dmI5UWg3X25lTkdweGlvalZWOTVHRUJBQnMuIiwic2NwIjoiZW1haWwgb3BlbmlkIHByb2ZpbGUgVXNlci5SZWFkIiwic3ViIjoiNFVOcE5rZkdpVTFLbFdJQTY5bmpMZElMQTBqbU9mMGo0MHRBa3IweGwtQSIsInRpZCI6IjAzMjU4ZjcyLWVlMzctNGJiYi1iMzhkLTZlZmRmYzk2Y2RlNiIsInVuaXF1ZV9uYW1lIjoiV0hUWloyQGN1bW1pbnNjaGluYS5wYXJ0bmVyLm9ubXNjaGluYS5jbiIsInVwbiI6IldIVFpaMkBjdW1taW5zY2hpbmEucGFydG5lci5vbm1zY2hpbmEuY24iLCJ1dGkiOiJpM3EwWXR5WEZrcUtrQUFrZVVJVEFBIiwidmVyIjoiMS4wIiwid2lkcyI6WyJiNzlmYmY0ZC0zZWY5LTQ2ODktODE0My03NmIxOTRlODU1MDkiXSwieG1zX3N0Ijp7InN1YiI6IkYxdDJNdFZRdUgtMS1YaVh6eXNPSEpVRTNxNmlsQnc3RGZ2Y1dja1VnT1kifSwieG1zX3RjZHQiOjE0NTQzODMzMTV9.yy7xO7kicwyE6CHs-SLuuxbF-QDrwYZ3EAd2z1HbBdnhRjLZcDOmNyb208IiyYyOsx4O16XzoA1KkIv6KZeaWg9qWfbD1JvWnmrqcns5E24qsU__gdKSfvvoYeu6yVRtSI-x_uE7gysHpf0EASaOJh7AekxD8EA3VbGxQNBLq8sESX5CqfdWb6vT59sthURsqGATmxouiMhz3ov1IXp4ngQ5iT_WVUEv65IN9itj3I56C5V7qUj6RxouisKQZZZ8XoT3XFkSn8EasIdREegJlBfnPzxNOjB9f8Vj7aymyZYOH7WLvPuBHo1Oiey5vzhYhG0vcWVCTdd78PX6QvNg-Q\",\"freshIndex\":34,\"pageNo\":1,\"pageSize\":10,\"teletextIds\":\"93,101,78,108,92,98,74,77,87,91,5,4079,3,20,6,4,1,2,24,7,9,14,12,10,16,13,15,8,11,29,35,30,32,33,34,38,36,89,90,2,20,24,4,4079,6,3,5,1\",\"userid\":\"577197A6C53B06731DCE305292365919\",\"videoIds\":\"1,1\",\"fun\":\"eCummins/getContentList\"}"
                )
                if (supportActionBar?.isShowing == true) {
                    supportActionBar?.hide()
                } else {
                    supportActionBar?.show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choose_night, menu)
        return true
    }

    override fun initContent(savedInstanceState: Bundle?) {
//        DiffUtil.calculateDiff(object : DiffUtil.Callback() {
//            override fun getOldListSize(): Int {
//            }
//
//            override fun getNewListSize(): Int {
//            }
//
//            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//            }
//
//            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
//
//            }
//
//            ]
//        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.light -> setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.night -> setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.system -> setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
        return false
    }
}