package chooongg.box.simple.modules.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import chooongg.box.core.activity.BoxViewBindingActivity
import chooongg.box.core.ext.setNightMode
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityMainBinding
import chooongg.box.simple.modules.main.adapter.MainAdapter
import chooongg.box.simple.modules.main.entity.MainItemEntity

class MainActivity : BoxViewBindingActivity<ActivityMainBinding>() {

    private val modules = arrayListOf(
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar),
        MainItemEntity("App Bar: Top", R.mipmap.ic_topappbar)
    )
    private val adapter = MainAdapter(modules)

    override fun initConfig(savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choose_night, menu)
        return true
    }

    override fun initContent(savedInstanceState: Bundle?) {
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