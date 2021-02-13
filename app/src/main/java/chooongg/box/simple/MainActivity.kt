package chooongg.box.simple

import android.Manifest
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import chooongg.box.ext.toastSuccess
import chooongg.box.ext.toastWarn

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                toastSuccess("成功")
            } else {
                toastWarn("失败")
            }
        }.launch(Manifest.permission.CAMERA)
    }
}