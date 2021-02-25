package chooongg.box.simple

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import chooongg.box.core.activity.BoxActivity
import chooongg.box.ext.doOnClick
import chooongg.box.log.BoxLog
import chooongg.box.log.LogBean

class MainActivity : BoxActivity(R.layout.activity_main) {

    override fun initConfig(savedInstanceState: Bundle?) {

        registerForActivityResult(ActivityResultContracts.RequestPermission()) {

        }.launch(Manifest.permission.CAMERA)

        findViewById<TextView>(R.id.tv_text).apply {
            text = System.currentTimeMillis().toString()
            doOnClick {
                BoxLog.e("测试")
                BoxLog.eTagChild(
                    "Test",
                    LogBean("TTT", "测试打印\n测试打印"),
                    supportActionBar?.title,
                    arrayOf("Asdfasdf", "asdfasdf", null),
                    Intent(Intent.ACTION_DIAL, Uri.parse("tel:4001790720l")),
                    Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("http://www.baidu.com/")
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    },
                    Bundle().apply {
                        putString("sdfa", "asdasdg")
                        putInt("wefsz", 17858)
                        putFloatArray("floatArray", floatArrayOf(0f, 1f, 2f, 3f))
                        putBundle("bundle", Bundle().apply {
                            putString("sdfa", "asdasdg")
                            putInt("wefsz", 17858)
                            putBundle("bundle", Bundle().apply {
                                putString("sdfa", "asdasdg")
                                putInt("wefsz", 17858)
                            })
                        })
                    }
                )
            }
        }
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}