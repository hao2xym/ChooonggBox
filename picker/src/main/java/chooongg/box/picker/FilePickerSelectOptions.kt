package chooongg.box.picker

import androidx.annotation.IntRange
import androidx.annotation.StyleRes
import chooongg.box.picker.model.CommonlyDirectory
import chooongg.box.picker.model.FilePickerSortType
import chooongg.box.picker.model.MediaItem

object FilePickerSelectOptions {

    @StyleRes
    var themeId = -1

    @IntRange(from = 1)
    var maxCount = 9
    val isSingle get() = maxCount == 1


    var onSelectFileListener: (() -> Unit)? = null
    var fileTypes: Array<out String> = arrayOf()
    var sortType = FilePickerSortType.NAME_ASC
    var commonlyDirectory = ArrayList<CommonlyDirectory>()
    var onlyShowCommonly = false
        set(value) {
            field = value
            if (field) onlyShowBrowser = false
        }
    var onlyShowBrowser = false
        set(value) {
            field = value
            if (field) onlyShowCommonly = false
        }


    var onSelectMediaListener: ((ArrayList<MediaItem>) -> Unit)? = null
    var isNumberSelect = true
    var needPreview = true
    var needCamera = false
    var needCrop = false
    var showGif = false
    var onlyShowImages = false
        set(value) {
            field = value
            if (field) onlyShowVideos = false
        }
    var onlyShowVideos = false
        set(value) {
            field = value
            if (field) onlyShowImages = false
        }
    var compressImage = true

    fun reset() {
        themeId = -1
        maxCount = 9

        onSelectFileListener = null
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

        onSelectMediaListener = null
        isNumberSelect = true
        needPreview = true
        needCamera = false
        needCrop = false
        onlyShowImages = false
        onlyShowVideos = false
        compressImage = true
    }
}