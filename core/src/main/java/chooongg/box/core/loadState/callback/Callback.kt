package chooongg.box.core.loadState.callback

import android.content.Context
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import chooongg.box.ext.doOnClick
import chooongg.box.ext.resourcesInteger
import java.io.*
import kotlin.reflect.KClass

abstract class Callback : Serializable {

    private var rootView: View? = null
    protected lateinit var context: Context
    private var onReloadListener: ((KClass<out Callback>) -> Unit)? = null
    var successViewVisible = false

    @LayoutRes
    protected open fun getViewLayout(): Int = 0

    protected open fun onBuildView(context: Context): View? = null

    protected abstract fun onViewCreated(context: Context, view: View)

    abstract fun onAttach(context: Context, view: View)

    open fun onReloadEvent(): View? = rootView

    open fun onTextChange(): TextView? = null

    open fun onImageChange(): AppCompatImageView? = null

    open fun setVerticalPercentage(percentage: Float) = Unit

    open fun setHorizontalPercentage(percentage: Float) = Unit

    abstract fun onDetach(context: Context, view: View)

    constructor()

    internal constructor(
        view: View,
        context: Context,
        onReloadListener: ((KClass<out Callback>) -> Unit)?
    ) {
        this.rootView = view
        this.context = context
        this.onReloadListener = onReloadListener
    }

    fun setCallback(context: Context, onReloadListener: ((KClass<out Callback>) -> Unit)?) = apply {
        this.context = context
        this.onReloadListener = onReloadListener
    }

    fun getRootView(): View {
        if (rootView != null) return rootView!!
        val resId: Int = getViewLayout()
        rootView = if (resId != 0) {
            View.inflate(context, getViewLayout(), null)
        } else onBuildView(context)

        onViewCreated(context, rootView!!)
        onReloadEvent()?.doOnClick {
            onReloadListener?.invoke(this::class)
        }
        return rootView!!
    }

    open fun isEnableAnimation() = true

    open fun getAnimation(): Animation {
        return AnimationSet(true).apply {
            addAnimation(AlphaAnimation(0f, 1f))
            addAnimation(
                ScaleAnimation(
                    0.9f, 1f, 0.9f, 1f,
                    Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f
                )
            )
            duration = context.resourcesInteger(android.R.integer.config_mediumAnimTime).toLong()
            interpolator = FastOutSlowInInterpolator()
        }
    }

    fun obtainRootView(): View {
        if (rootView == null) {
            rootView = View.inflate(context, getViewLayout(), null)
        }
        return rootView!!
    }

    fun copy(): Callback {
        val bao = ByteArrayOutputStream()
        var obj: Any? = null
        try {
            val oos = ObjectOutputStream(bao)
            oos.writeObject(this)
            oos.close()
            val bis = ByteArrayInputStream(bao.toByteArray())
            val ois = ObjectInputStream(bis)
            obj = ois.readObject()
            ois.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return obj as Callback
    }
}