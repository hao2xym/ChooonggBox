package chooongg.box.picker

import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import chooongg.box.picker.model.CommonlyDirectory
import chooongg.box.picker.model.FilePickerSortType

object FilePickerSelectOptions {

    @IntRange(from = 1)
    var maxCount = 9
    val isSingle get() = maxCount == 1


    var fileTypes: Array<out String> = arrayOf()
    var sortType = FilePickerSortType.NAME_ASC
    var commonlyDirectory = ArrayList<CommonlyDirectory>()
    var onlyShowCommonly = false
    var onlyShowBrowser = false

    var enableCamera = true
    var onlyShowImages = false
    var onlyShowVideos = false
    var compressImage = true

    @StyleRes
    var themeId = -1

    fun reset() {
        maxCount = 9
        fileTypes = arrayOf()
        sortType = FilePickerSortType.NAME_ASC
        commonlyDirectory = arrayListOf(
            CommonlyDirectory(
                "微信", "com.tencent.mm", arrayOf(
                    "/Download/WeiXin/",
                    "/Android/data/com.tencent.mm/MicroMsg/Download/"
                )
            ),
            CommonlyDirectory(
                "钉钉", "com.alibaba.android.rimet", arrayOf(
                    "/DingTalk/",
                )
            ),
            CommonlyDirectory(
                "QQ", "com.tencent.mobileqq", arrayOf(
                    "/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/"
                )
            ),
        )
        onlyShowCommonly = false
        onlyShowBrowser = false

        enableCamera = true
        onlyShowImages = false
        onlyShowVideos = false
        compressImage = true
        themeId = -1
    }
}