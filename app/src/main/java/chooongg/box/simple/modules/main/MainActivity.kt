package chooongg.box.simple.modules.main

import android.content.Intent
import android.os.Bundle
import android.transition.Explode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import chooongg.box.core.activity.BoxVBVMActivity
import chooongg.box.ext.isNightMode
import chooongg.box.ext.setNightMode
import chooongg.box.ext.showToast
import chooongg.box.log.BoxLog
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityMainBinding
import chooongg.box.simple.modules.appBarTop.TopAppBarActivity
import chooongg.box.simple.modules.loadState.LoadStateActivity
import chooongg.box.simple.modules.main.entity.MainItemEntity
import chooongg.box.simple.modules.main.entity.MainViewModel
import chooongg.box.simple.modules.permission.RequestPermissionActivity
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.IAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter

class MainActivity : BoxVBVMActivity<ActivityMainBinding, MainViewModel>() {

    private val modules = arrayListOf(
        MainItemEntity("App Bar: Top"),
        MainItemEntity("Request Permissions"),
        MainItemEntity("Load State")
    )

    override fun isAutoShowNavigationIcon() = false

    override fun initTransition() {
        window.exitTransition = Explode()
        window.enterTransition = Explode()
    }

    private val itemAdapter = ItemAdapter<MainItemEntity>()
    private val adapter = FastAdapter.with(itemAdapter)

    override fun initConfig(savedInstanceState: Bundle?) {
        BoxLog.e("isNightMode=${isNightMode()}")
        supportActionBar?.setLogo(R.mipmap.ic_launcher)
        binding.recyclerView.adapter = adapter
        itemAdapter.setNewList(modules)
        adapter.onClickListener =
            { _: View?, _: IAdapter<MainItemEntity>, mainItemEntity: MainItemEntity, i: Int ->
                when (mainItemEntity.name) {
                    "App Bar: Top" -> startActivity(
                        Intent(context, TopAppBarActivity::class.java)
                    )
                    "Request Permissions" -> startActivity(
                        Intent(context, RequestPermissionActivity::class.java)
                    )
                    "Load State" -> startActivity(
                        Intent(context, LoadStateActivity::class.java)
                    )
                    else -> showToast("未实现功能")
                }
                false
            }
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

    private var firstTime: Long = 0

    override fun onBackPressed() {
        val secondTime = System.currentTimeMillis()
        if (secondTime - firstTime > 2000) {
            showToast("    再按一次退出程序    ", Toast.LENGTH_SHORT)
            firstTime = secondTime
        } else {
            super.onBackPressed()
        }
    }

}