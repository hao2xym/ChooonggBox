package chooongg.box.core.statePage.state

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import chooongg.box.core.R
import chooongg.box.core.databinding.StatePageDefaultBinding
import chooongg.box.core.statePage.StatePageLayout
import chooongg.box.ext.APP
import chooongg.box.ext.doOnClick
import chooongg.box.ext.visible

class NetworkState : MultiState() {

    private lateinit var binding: StatePageDefaultBinding

    override fun onCreateMultiStateView(
        context: Context,
        container: StatePageLayout
    ): View {
        binding = StatePageDefaultBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onMultiStateViewCreate(view: View) {
        binding.ivImage.setImageResource(R.drawable.ic_state_network)
        binding.tvMessage.text = "请检查网络连接"
        binding.btnOperate.visible()
        binding.btnOperate.setText(R.string.state_network_setting)
        binding.btnOperate.doOnClick {
            APP.startActivity(Intent(Settings.ACTION_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    override fun setText(text: CharSequence) {
        binding.tvMessage.text = text
    }

    override fun getText() = ""

    override fun enableReload() = false
}