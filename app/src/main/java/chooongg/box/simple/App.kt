package chooongg.box.simple

import chooongg.box.BoxApplication


class App : BoxApplication() {
    override fun onCreate() {
        super.onCreate()
        if (!isAppMainProcess()) return
    }

}