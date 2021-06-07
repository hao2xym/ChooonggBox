package chooongg.box.core.statePage

import chooongg.box.core.statePage.state.MultiState

interface OnRetryEventListener {
    fun onRetryEvent(state: MultiState)
}