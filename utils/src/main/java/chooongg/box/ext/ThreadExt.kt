package chooongg.box.ext

import android.os.Looper

fun isMainThread() = Looper.myLooper() == Looper.getMainLooper()