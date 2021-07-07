package chooongg.box.manager

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import chooongg.box.ext.getMimeType
import chooongg.box.log.BoxLog

object FileOpener {

    /**
     * 直接打开 Url 对应的系统应用
     * 如果url是视频地址,则直接用系统的播放器打开
     */
    fun openUrl(activity: Activity, uri: Uri?) {
        try {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            intent.setDataAndType(uri, uri.getMimeType())
            activity.startActivity(intent)
        } catch (e: Exception) {
            BoxLog.e("openUrl error : ${e.message}")
        }
    }

    /**
     * 打开系统分享弹窗 (Open the system sharing popup)
     */
    fun openShare(context: Context, uri: Uri, title: String = "分享文件") {
        val intent = Intent(Intent.ACTION_SEND)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        // Put the Uri and MIME type in the result Intent
        intent.setDataAndType(uri, uri.getMimeType())
        //https://stackoverflow.com/questions/3918517/calling-startactivity-from-outside-of-an-activity-context
        val chooserIntent: Intent = Intent.createChooser(intent, title)
        chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooserIntent)
    }
}