package chooongg.box.simple

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import chooongg.box.ext.doOnClick
import chooongg.box.logger.BoxLog
import chooongg.box.logger.formatter.DefaultFormatter

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

        val msg = buildString {
            for (i in 0..10) {
                if (i == 0) {
                    append(DefaultFormatter.top("Request", null))
                } else {
                    append(DefaultFormatter.middle("测试中文测试中文测试中文测试中文测试中文测试中文测试中文测试中文测试中文测试中文测试中文测试中文测试中文 ${i}"))
                }
                append(DefaultFormatter.separator())
            }
            append(DefaultFormatter.bottom())
        }

        findViewById<TextView>(R.id.tv_text).apply {
            text = msg.length.toString()
            doOnClick {
                BoxLog.eTagChild("测试打印", msg, msg)
            }
        }
    }
}