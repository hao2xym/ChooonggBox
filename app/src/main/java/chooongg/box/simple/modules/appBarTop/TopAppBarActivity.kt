package chooongg.box.simple.modules.appBarTop

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatDelegate
import chooongg.box.core.activity.BoxBindingActivity
import chooongg.box.ext.setNightMode
import chooongg.box.simple.R
import chooongg.box.simple.databinding.ActivityTopAppbarBinding

class TopAppBarActivity : BoxBindingActivity<ActivityTopAppbarBinding>() {

    override fun initConfig(savedInstanceState: Bundle?) {

    }

    override fun initContent(savedInstanceState: Bundle?) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.choose_night, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.light -> setNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            R.id.night -> setNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            R.id.system -> setNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            else -> return super.onOptionsItemSelected(item)
        }
        return false
    }
}