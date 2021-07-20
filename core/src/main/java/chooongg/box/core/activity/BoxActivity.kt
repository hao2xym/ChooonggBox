package chooongg.box.core.activity

import android.animation.Animator
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.transition.Fade
import android.transition.Slide
import android.transition.Transition
import android.transition.TransitionSet
import android.view.Gravity
import android.view.MenuItem
import android.view.Window
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ContentFrameLayout
import androidx.appcompat.widget.FitWindowsLinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import chooongg.box.core.R
import chooongg.box.core.databinding.BoxActivityLoadingTipBinding
import chooongg.box.core.interfaces.BoxInit
import chooongg.box.core.manager.HideKeyboardManager
import chooongg.box.core.widget.BoxToolBar
import chooongg.box.ext.decorView
import chooongg.box.ext.gone
import chooongg.box.ext.launchMain
import chooongg.box.ext.visible
import chooongg.box.log.BoxLog
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BoxActivity(@LayoutRes private val contentLayoutId: Int? = null) :
    AppCompatActivity(), BoxInit {

    val contentView: ContentFrameLayout by lazy { findViewById(Window.ID_ANDROID_CONTENT) }

    inline val context: Context get() = this

    inline val activity: Activity get() = this

    protected open fun isAutoHideKeyBoard() = true

    private var loadingTipBinding: BoxActivityLoadingTipBinding? = null

    protected var toolbar: Toolbar? = null

    open fun isShowActionBar() = true

    protected open fun getToolBar(parentLayout: FitWindowsLinearLayout) = layoutInflater.inflate(
        R.layout.box_activity_toolbar,
        parentLayout,
        false
    ) as BoxToolBar

    protected open fun initToolBar(toolbar: Toolbar) = Unit

    open fun isAutoShowNavigationIcon() = true

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        configThemeForAnnotation()
        super.onCreate(savedInstanceState)
        BoxLog.d("${this::class.simpleName}: onCreate")
        window.enterTransition = initEnterTransition()
        window.exitTransition = initExitTransition()
        window.returnTransition = initReturnTransition()
        window.reenterTransition = initReenterTransition()
        window.sharedElementEnterTransition = initEnterTransition()
        window.sharedElementExitTransition = initExitTransition()
        window.sharedElementReturnTransition = initReturnTransition()
        window.sharedElementReenterTransition = initReenterTransition()
        if (isShowActionBar()) configActionBar()
        if (toolbar != null) initToolBar(toolbar!!)
        if (contentLayoutId != null) {
            setContentView(contentLayoutId)
        }
        onCreateToInitConfig(savedInstanceState)
        if (isAutoHideKeyBoard()) HideKeyboardManager.init(activity)
        if (toolbar != null) setSupportActionBar(toolbar)
    }

    protected open fun configActionBar() {
        val parentLayout = contentView.parent as FitWindowsLinearLayout
        toolbar = getToolBar(parentLayout).apply {
            id = R.id.activity_box_toolbar
        }
        parentLayout.addView(toolbar, 0)
    }

    @CallSuper
    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        onPostCreateToInitContent(savedInstanceState)
    }

    private fun configThemeForAnnotation() {
        if (javaClass.isAnnotationPresent(Theme::class.java)) {
            setTheme(javaClass.getAnnotation(Theme::class.java)!!.value)
        }
    }

    protected open fun onCreateToInitConfig(savedInstanceState: Bundle?) {
        initConfig(savedInstanceState)
    }

    protected open fun onPostCreateToInitContent(savedInstanceState: Bundle?) {
        initContent(savedInstanceState)
    }

    // 启动时-进入动画
    protected open fun initEnterTransition(): Transition = TransitionSet().apply {
        addTransition(Fade().apply {
            excludeChildren(android.R.id.content, true)
        })
        addTransition(Slide(Gravity.END).apply {
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            excludeTarget(BottomNavigationView::class.java, true)
            excludeTarget(MaterialToolbar::class.java, true)
            excludeTarget(Toolbar::class.java, true)
        })
    }

    // 启动时-退出动画
    protected open fun initExitTransition(): Transition = Fade().apply {
        excludeChildren(android.R.id.content, true)
        excludeTarget(android.R.id.statusBarBackground, true)
        excludeTarget(android.R.id.navigationBarBackground, true)
        excludeTarget(MaterialToolbar::class.java, true)
        excludeTarget(Toolbar::class.java, true)
    }

    // 退出时-退出动画
    protected open fun initReturnTransition(): Transition = TransitionSet().apply {
        addTransition(Fade().apply {
            excludeChildren(android.R.id.content, true)
        })
        addTransition(Slide(Gravity.END).apply {
            excludeTarget(android.R.id.statusBarBackground, true)
            excludeTarget(android.R.id.navigationBarBackground, true)
            excludeTarget(BottomNavigationView::class.java, true)
            excludeTarget(MaterialToolbar::class.java, true)
            excludeTarget(Toolbar::class.java, true)
        })
    }

    // 退出时-进入动画
    protected open fun initReenterTransition(): Transition = Fade().apply {
        excludeChildren(android.R.id.content, true)
        excludeTarget(android.R.id.statusBarBackground, true)
        excludeTarget(android.R.id.navigationBarBackground, true)
        excludeTarget(MaterialToolbar::class.java, true)
        excludeTarget(Toolbar::class.java, true)
    }

    override fun setSupportActionBar(toolbar: Toolbar?) {
        super.setSupportActionBar(toolbar)
        if (isAutoShowNavigationIcon()) {
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_app_bar_back)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun showTipLoading(message: CharSequence? = null, isClickable: Boolean = false) {
        lifecycleScope.launchMain {
            if (loadingTipBinding != null) {
                if (message.isNullOrEmpty()) {
                    loadingTipBinding!!.tvMessage.gone()
                } else {
                    loadingTipBinding!!.tvMessage.text = message
                    loadingTipBinding!!.tvMessage.visible()
                }
                return@launchMain
            }
            loadingTipBinding = BoxActivityLoadingTipBinding.inflate(layoutInflater)
            loadingTipBinding!!.root.layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            loadingTipBinding!!.progressView.show()
            if (message.isNullOrEmpty()) {
                loadingTipBinding!!.tvMessage.gone()
            } else {
                loadingTipBinding!!.tvMessage.text = message
                loadingTipBinding!!.tvMessage.visible()
            }
            if (!isClickable) loadingTipBinding!!.root.setOnClickListener { }
            decorView.addView(loadingTipBinding!!.root)
            loadingTipBinding!!.root.animate().alpha(1f).scaleX(1f).scaleY(1f).setListener(null)
        }
    }

    fun hideTipLoading() {
        if (!isDestroyed) {
            lifecycleScope.launchMain {
                if (loadingTipBinding == null) return@launchMain
                loadingTipBinding!!.root.animate().alpha(0f).scaleX(0.8f).scaleY(0.8f)
                    .setListener(object : Animator.AnimatorListener {
                        override fun onAnimationStart(animation: Animator?) = Unit
                        override fun onAnimationRepeat(animation: Animator?) = Unit
                        override fun onAnimationCancel(animation: Animator?) = Unit
                        override fun onAnimationEnd(animation: Animator?) {
                            decorView.removeView(loadingTipBinding!!.root)
                            loadingTipBinding = null
                        }
                    })
            }
        }
    }
}