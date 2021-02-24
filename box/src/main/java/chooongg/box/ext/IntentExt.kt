package chooongg.box.ext

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission

/**
 * 通知设置意图
 */
@RequiresApi(Build.VERSION_CODES.O)
fun intentNotificationSetting(channel: String? = null, packageName: String = APP.packageName) =
    Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS).apply {
        putExtra(Settings.EXTRA_APP_PACKAGE, packageName)
        putExtra(Settings.EXTRA_CHANNEL_ID, channel)
    }

/**
 * 拨号界面意图
 */
fun intentDial(tel: String?) = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${tel ?: ""}"))

/**
 * 拨号界面意图
 */
@RequiresPermission(Manifest.permission.CALL_PHONE)
fun intentCall(tel: String?) = Intent(Intent.ACTION_CALL, Uri.parse("tel:${tel ?: ""}"))

/**
 * 浏览器意图
 */
fun intentBrowser(url: String?) = Intent(Intent.ACTION_VIEW, Uri.parse(url))

/**
 * 系统设置意图
 */
fun intentSystemSetting() = Intent(Settings.ACTION_SETTINGS)

/**
 * 网络设置意图
 */
fun intentNetworkSetting() = Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS)

/**
 * WIFI设置意图
 */
fun intentWifiSetting() = Intent(Settings.ACTION_WIFI_SETTINGS)

/**
 * 移动网络意图
 */
fun intentMobileNetworkSetting() = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)