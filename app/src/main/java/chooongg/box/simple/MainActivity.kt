package chooongg.box.simple

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.safframework.utils.showToast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                showToast(this, "成功", Toast.LENGTH_LONG)
            } else {
                showToast(this, "失败", Toast.LENGTH_LONG)
            }
        }.launch(Manifest.permission.CAMERA)
    }
}