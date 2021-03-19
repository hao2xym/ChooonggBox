package chooongg.box.entity

import androidx.appcompat.app.AppCompatDelegate
import chooongg.box.Box
import chooongg.box.mmkv.MMKVController
import chooongg.box.mmkv.MMKVKey

object BoxMMKVConst : MMKVController(Box.TAG) {
    object DayNightMode : MMKVKey<Int>(this, "dayNightMode", AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
}