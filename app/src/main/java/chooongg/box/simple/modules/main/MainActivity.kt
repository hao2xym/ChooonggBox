package chooongg.box.simple.modules.main

import android.os.Bundle
import android.util.TypedValue
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.core.adapter.BindingAdapter
import chooongg.box.core.adapter.BindingHolder
import chooongg.box.ext.isNightMode
import chooongg.box.ext.setNightMode
import chooongg.box.ext.showToast
import chooongg.box.ext.startActivity
import chooongg.box.http.ext.requestDefault
import chooongg.box.log.BoxLog
import chooongg.box.picker.FilePicker
import chooongg.box.simple.BuildConfig
import chooongg.box.simple.R
import chooongg.box.simple.api.WanAndroidAPI
import chooongg.box.simple.databinding.ActivityMainBinding
import chooongg.box.simple.databinding.ItemMainBinding
import chooongg.box.simple.modules.appBarTop.TopAppBarActivity
import chooongg.box.simple.modules.loadState.StatePageActivity
import chooongg.box.simple.modules.main.entity.MainItemEntity
import chooongg.box.simple.modules.permission.RequestPermissionActivity
import com.fondesa.recyclerviewdivider.dividerBuilder
import kotlinx.coroutines.launch

class MainActivity : BoxBindingActivity<ActivityMainBinding>() {

    private val modules = arrayListOf(
        MainItemEntity("App Bar: Top"),
        MainItemEntity("Request Permissions"),
        MainItemEntity("State Page"),
        MainItemEntity("File Picker"),
        MainItemEntity("Media Picker Single"),
        MainItemEntity("Media Picker Multiple"),
        MainItemEntity("Media Picker All"),
        MainItemEntity("Media Picker Image"),
        MainItemEntity("Media Picker Video"),
        MainItemEntity("Request Http")
    )

    override fun isShowActionBar() = false

    override fun isAutoShowNavigationIcon() = false

    private val adapter = Adapter()

    override fun initConfig(savedInstanceState: Bundle?) {
        supportActionBar?.subtitle = BuildConfig.VERSION_NAME
        dividerBuilder().asSpace().showFirstDivider().showLastDivider()
            .size(16, TypedValue.COMPLEX_UNIT_DIP)
            .build().addTo(binding.recyclerView)
        binding.recyclerView.adapter = adapter
        adapter.setNewInstance(modules)
        adapter.setOnItemClickListener { _, view, position ->
            when (modules[position].name) {
                "App Bar: Top" -> startActivity(TopAppBarActivity::class, view)
                "Request Permissions" -> startActivity(RequestPermissionActivity::class)
                "State Page" -> startActivity(StatePageActivity::class, view)
                "File Picker" -> FilePicker.from(this).chooseFile().start { }
                "Media Picker Single" -> FilePicker.from(this).chooseMedia()
                    .singleChoose()
                    .start { }
                "Media Picker Multiple" -> FilePicker.from(this).chooseMedia()
                    .maxCount(9)
                    .start { }
                "Media Picker All" -> FilePicker.from(this).chooseMedia()
                    .needCamera(true)
                    .start { }
                "Media Picker Image" -> FilePicker.from(this).chooseMedia()
                    .onlyShowImages()
                    .start { }
                "Media Picker Video" -> FilePicker.from(this).chooseMedia()
                    .onlyShowVideos()
                    .start { }
                "Request Http" -> lifecycleScope.launch {
                    requestDefault<ArrayList<Any>> {
                        api { WanAndroidAPI.get().allPackage() }
                    }
                }
                else -> showToast("未实现功能")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choose_night, menu)
        return true
    }

    override fun initContent(savedInstanceState: Bundle?) {

    }

    class Adapter : BindingAdapter<MainItemEntity, ItemMainBinding>() {
        override fun convert(holder: BindingHolder<ItemMainBinding>, item: MainItemEntity) {
            holder.binding.tvName.text = item.name
        }
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