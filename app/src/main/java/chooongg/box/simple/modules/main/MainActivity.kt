package chooongg.box.simple.modules.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.Keep
import androidx.appcompat.app.AppCompatDelegate
import chooongg.box.core.activity.BoxBindingModelActivity
import chooongg.box.ext.isNightMode
import chooongg.box.ext.setNightMode
import chooongg.box.ext.showToast
import chooongg.box.ext.startActivity
import chooongg.box.http.ext.ResponseData
import chooongg.box.http.ext.RetrofitCoroutinesSimpleDsl
import chooongg.box.http.throws.HttpException
import chooongg.box.log.BoxLog
import chooongg.box.picker.FilePicker
import chooongg.box.simple.BuildConfig
import chooongg.box.simple.R
import chooongg.box.simple.api.WanAndroidAPI
import chooongg.box.simple.databinding.ActivityMainBinding
import chooongg.box.simple.modules.appBarTop.TopAppBarActivity
import chooongg.box.simple.modules.loadState.StatePageActivity
import chooongg.box.simple.modules.main.entity.MainItemEntity
import chooongg.box.simple.modules.main.entity.MainViewModel
import chooongg.box.simple.modules.permission.RequestPermissionActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin

class MainActivity : BoxBindingModelActivity<ActivityMainBinding, MainViewModel>() {

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
    )

    override fun isAutoShowNavigationIcon() = false

    private val adapter = Adapter()

    @Keep
    data class WanAndroidAPIResponse<DATA>(
        val errorCode: Int,
        val errorMsg: String?,
        val data: DATA?
    ) : ResponseData<DATA> {
        override suspend fun checkData(): DATA {
            if (errorCode == 0) {
                if (data == null) throw HttpException(HttpException.Type.EMPTY)
                return data
            } else {
                throw HttpException(
                    errorCode.toString(),
                    errorMsg ?: HttpException.Converter.convert(HttpException.Type.UN_KNOWN)
                )
            }
        }
    }

    class RetrofitCoroutinesDsl<DATA> :
        RetrofitCoroutinesSimpleDsl<WanAndroidAPIResponse<DATA>, DATA>()

    @Suppress("DEPRECATION")
    suspend fun <DATA> request(block: RetrofitCoroutinesDsl<DATA>.() -> Unit) {
        val dsl = RetrofitCoroutinesDsl<DATA>()
        block.invoke(dsl)
        dsl.executeRequest()
    }

    private var job: Job? = null

    override fun initConfig(savedInstanceState: Bundle?) {
        BoxLog.e("isNightMode=${isNightMode()}")
        supportActionBar?.subtitle = BuildConfig.VERSION_NAME
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
                    .start { }
                "Media Picker Image" -> FilePicker.from(this).chooseMedia()
                    .onlyShowImages()
                    .start { }
                "Media Picker Video" -> FilePicker.from(this).chooseMedia()
                    .onlyShowVideos()
                    .start { }
                else -> showToast("未实现功能")
            }
        }
    }

    suspend fun requestss() {
        request<ArrayList<String>> {
            api { WanAndroidAPI.get().allPackage() }
            onStart {
                job?.cancelAndJoin()
            }
            onSuccess {
                BoxLog.e(it)
            }
            onFailed {
                it.printStackTrace()
            }
            onEnd {

            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choose_night, menu)
        return true
    }

    override fun initContent(savedInstanceState: Bundle?) {

    }

    class Adapter : BaseQuickAdapter<MainItemEntity, BaseViewHolder>(R.layout.item_main) {
        override fun convert(holder: BaseViewHolder, item: MainItemEntity) {
            holder.setText(R.id.tv_name, item.name)
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