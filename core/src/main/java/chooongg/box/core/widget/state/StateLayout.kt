package chooongg.box.core.widget.state

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.Keep
import kotlin.reflect.KClass

class StateLayout : FrameLayout {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)
    constructor(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {

    }

    private var onStateEventListener: OnStateEventListener? = null

    private var stateViews = HashMap<KClass<out StateView>, StateView>()

    lateinit var currentStateView: StateView
        private set

    constructor(contentView: View) : super(contentView.context) {
        val contentStateView = ContentStateView(contentView)
        stateViews[ContentStateView::class] = contentStateView
        currentStateView = contentStateView
    }

    fun setOnStateEventListener(block: (stateView: KClass<*>, view: View) -> Unit) {
        onStateEventListener = object : OnStateEventListener {
            override fun event(stateView: KClass<*>, view: View) {
                block.invoke(stateView, view)
            }
        }
    }

    @Keep
    interface OnStateEventListener {
        fun event(stateView: KClass<*>, view: View)
    }
}