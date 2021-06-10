package chooongg.box.simple

import chooongg.box.BoxApplication
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import me.dkzwm.widget.srl.IRefreshViewCreator
import me.dkzwm.widget.srl.SmoothRefreshLayout
import me.dkzwm.widget.srl.extra.IRefreshView
import me.dkzwm.widget.srl.extra.footer.ClassicFooter
import me.dkzwm.widget.srl.extra.header.ClassicHeader
import me.dkzwm.widget.srl.indicator.IIndicator

class App : BoxApplication() {

    companion object {
        init {
            SmoothRefreshLayout.setDefaultCreator(object : IRefreshViewCreator {
                override fun createHeader(layout: SmoothRefreshLayout): IRefreshView<IIndicator> {
                    return ClassicHeader(layout.context)
                }

                override fun createFooter(layout: SmoothRefreshLayout): IRefreshView<IIndicator> {
                    return ClassicFooter(layout.context)
                }
            })
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (!isAppMainProcess()) return
        // 友盟
        UMConfigure.setProcessEvent(true)
        UMConfigure.setLogEnabled(BuildConfig.DEBUG)
        UMConfigure.init(this, UMConfigure.DEVICE_TYPE_PHONE, null)
        MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
    }

}