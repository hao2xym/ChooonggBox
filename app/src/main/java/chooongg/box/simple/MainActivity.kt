package chooongg.box.simple

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import chooongg.box.ext.doOnClick
import chooongg.box.log.BoxLog

class MainActivity : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
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

        findViewById<TextView>(R.id.tv_text).apply {
            text = System.currentTimeMillis().toString()
            doOnClick {
                BoxLog.eTagChild("测试打印", "测试打印\n测试打印", "测试打印")
            }
        }
    }
}