package chooongg.box.ext

import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun AppCompatActivity.requestPermission(permission: String) {
    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) return
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        when {
            it -> {

            }
            shouldShowRequestPermissionRationale(permission) -> {

            }
            else -> {

            }
        }
    }
}

fun Fragment.requestPermission(permission: String) {
    registerForActivityResult(ActivityResultContracts.RequestPermission()) {

    }
}

fun AppCompatActivity.requestMultiplePermission() {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

    }
}

fun Fragment.requestMultiplePermission() {
    registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {

    }
}

interface PermissionCallback {

}

interface MultiplePermissionCallback {

}