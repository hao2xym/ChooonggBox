package chooongg.box.core.statePage

import android.view.animation.Animation
import chooongg.box.core.statePage.state.LoadingState
import chooongg.box.core.statePage.state.MultiState
import kotlin.reflect.KClass

class StatePageConfig {

    var enableAnimation = true

    var animation: Animation? = null

    var defaultState: KClass<out MultiState> = LoadingState::class

}