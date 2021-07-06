package chooongg.box.picker

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import chooongg.box.picker.PickerManager
import chooongg.box.picker.R
import com.google.android.material.appbar.MaterialToolbar

abstract class BaseFilePickerActivity : AppCompatActivity() {

    protected fun onCreate(savedInstanceState: Bundle?, @LayoutRes layout: Int) {
        super.onCreate(savedInstanceState)
        setTheme(PickerManager.theme)
        setContentView(layout)

        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //set orientation
        requestedOrientation = PickerManager.orientation
        initView()
    }

    protected abstract fun initView()
}
