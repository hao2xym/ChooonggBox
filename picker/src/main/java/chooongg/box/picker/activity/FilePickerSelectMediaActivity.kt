package chooongg.box.picker.activity

import android.os.Bundle
import chooongg.box.core.activity.BoxActivity
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions

class FilePickerSelectMediaActivity : BoxActivity() {
    override fun initConfig(savedInstanceState: Bundle?) {
        XXPermissions.with(context).permission(
            Permission.MANAGE_EXTERNAL_STORAGE,
            Permission.READ_EXTERNAL_STORAGE,
            Permission.WRITE_EXTERNAL_STORAGE
        )
            .request { permissions, all ->

        }
    }

    override fun initContent(savedInstanceState: Bundle?) {
    }
}