package chooongg.box.simple

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//            if (it) {
//                showToast("成功")
//            } else {
//                showToast("失败")
//            }
        }.launch(Manifest.permission.CAMERA)
    }
}