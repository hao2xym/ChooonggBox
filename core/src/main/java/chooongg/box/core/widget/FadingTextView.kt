package chooongg.box.core.widget

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.ArrayRes
import chooongg.box.core.R
import com.google.android.material.textview.MaterialTextView
import kotlin.math.abs

class FadingTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : MaterialTextView(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_TIME_OUT = 15000
    }

    private var fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
    private var fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)

    private val mHandler = Handler(Looper.getMainLooper())
    private var isShow = true
    private var isStopped = false
    private var position = 0

    private var texts: Array<out CharSequence>
        get

    private var timeout: Int = DEFAULT_TIME_OUT
        get
        set(value) {
            if (timeout < 1) {
                throw IllegalArgumentException("Timeout must be longer than 0")
            } else {
                field = value
            }
        }
    private var shuffle: Boolean

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.FadingTextView)
        texts = a.getTextArray(R.styleable.FadingTextView_texts)
        timeout = abs(a.getInteger(R.styleable.FadingTextView_timeout, 14500)) +
                resources.getInteger(android.R.integer.config_mediumAnimTime)
        shuffle = a.getBoolean(R.styleable.FadingTextView_shuffle, false)
        if (shuffle) shuffle()
        a.recycle()
    }

    fun setTexts(texts: Array<CharSequence>) {
        if (texts.isEmpty()) {
            throw IllegalArgumentException("There must be at least one text")
        } else {
            this.texts = texts

        }
    }

    fun setTexts(@ArrayRes resId: Int) {
        if (texts.isEmpty()) {
            throw IllegalArgumentException("There must be at least one text")
        } else {
            this.texts = resources.getStringArray(resId)

        }
    }

    fun resume() {
        isShow = true
        startAnimation()
    }

    fun pause() {
        isShow = false
        stopAnimation()
    }

    fun stop() {
        isShow = false
        isStopped = true
        stopAnimation()
    }

    fun restart() {
        isShow = true
        isStopped = false
        startAnimation()
        invalidate()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        pause()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resume()
    }

    fun forceRefresh() {
        stopAnimation()
        startAnimation()
    }

    fun shuffle() {
        val newTexts = texts.toMutableList()
        newTexts.shuffle()
        texts = newTexts.toTypedArray()
    }

    override fun startAnimation(animation: Animation) {
        if (isShow && !isStopped) super.startAnimation(animation)
    }

    protected fun startAnimation() {
        if (isInEditMode) return
        text = texts[position]
        startAnimation(fadeInAnimation)
        mHandler.postDelayed({
            startAnimation(fadeOutAnimation)
            animation?.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(animation: Animation?) = Unit
                override fun onAnimationRepeat(animation: Animation?) = Unit
                override fun onAnimationEnd(animation: Animation?) {
                    if (!isShow) return
                    position = if (position == texts.lastIndex) 0 else position + 1
                    startAnimation()
                }
            })
        }, timeout.toLong())
    }

    fun stopAnimation() {
        mHandler.removeCallbacksAndMessages(null)
        animation?.cancel()
    }
}