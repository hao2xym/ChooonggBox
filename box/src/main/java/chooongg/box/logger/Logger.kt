package chooongg.box.logger

import android.util.Log
import androidx.annotation.IntDef

object Logger {

    @IntDef(Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, Log.ERROR, Log.ASSERT)
    annotation class LogLevel


}