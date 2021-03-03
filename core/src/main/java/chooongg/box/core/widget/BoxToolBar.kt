package chooongg.box.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import chooongg.box.core.R
import chooongg.box.ext.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textview.MaterialTextView

@SuppressLint("CustomViewStyleable")
class BoxToolBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = R.style.BoxWidget_Toolbar_PrimarySurface
) : MaterialToolbar(context, attrs, defStyleAttr) {

    private var isCenterTitle: Boolean = false
    private var mTitleTextAppearance: Int
    private var mTitleTextColor: ColorStateList? = null
    private var mSubtitleTextAppearance: Int
    private var mSubtitleTextColor: ColorStateList? = null

    private var centerTitleLayout: LinearLayout? = null
    private var centerLogoView: AppCompatImageView? = null
    private var centerTitleView: TextView? = null
    private var centerSubtitleView: TextView? = null

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.BoxToolBar, defStyleAttr, defStyleRes)
        isCenterTitle = a.getBoolean(
            R.styleable.BoxToolBar_isCenterTitle,
            context.attrBoolean(R.attr.toolbarCenterTitle, false)
        )
        mTitleTextAppearance = a.getResourceId(R.styleable.Toolbar_titleTextAppearance, 0)
        if (a.hasValue(R.styleable.Toolbar_titleTextColor)) {
            mTitleTextColor = a.getColorStateList(R.styleable.Toolbar_titleTextColor)
        }
        mSubtitleTextAppearance = a.getResourceId(R.styleable.Toolbar_subtitleTextAppearance, 0)
        if (a.hasValue(R.styleable.Toolbar_subtitleTextColor)) {
            mSubtitleTextColor = a.getColorStateList(R.styleable.Toolbar_subtitleTextColor)
        }
        a.recycle()
        configCenter()
    }

    fun changeCenterTitle(isCenterTitle: Boolean) {
        this.isCenterTitle = isCenterTitle
        configCenter()
    }

    @Suppress("DEPRECATION")
    private fun configCenter() {
        contentInsetEndWithActions = 0
        contentInsetStartWithNavigation = 0
        if (isCenterTitle) {
            setContentInsetsRelative(0, 0)
            setContentInsetsRelative(0, 0)
            if (centerTitleLayout == null) {
                centerTitleLayout = LinearLayout(context).apply {
                    orientation = LinearLayout.HORIZONTAL
                    layoutParams = LayoutParams(
                        LayoutParams.WRAP_CONTENT,
                        LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER
                    )
                    gravity = Gravity.CENTER

                    centerLogoView = AppCompatImageView(context).apply {
                        adjustViewBounds = true
                        maxWidth = context.attrDimensionPixelSize(R.attr.actionBarSize, dp2px(56f))
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        ).apply {
                            setMargins(0, dp2px(4f), dp2px(4f), dp2px(4f))
                        }
                    }
                    addView(centerLogoView)

                    val titleLayout = LinearLayout(context).apply {
                        orientation = LinearLayout.VERTICAL
                        layoutParams = LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT,
                            Gravity.CENTER
                        )
                        gravity = Gravity.CENTER
                    }

                    centerTitleView = TextView(context).apply {
                        setSingleLine()
                        ellipsize = TextUtils.TruncateAt.END
                        if (mTitleTextAppearance != Resources.ID_NULL) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                setTextAppearance(mTitleTextAppearance)
                            } else setTextAppearance(context, mTitleTextAppearance)
                        }
                        if (mTitleTextColor != null) {
                            setTextColor(mTitleTextColor)
                        }
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    titleLayout.addView(centerTitleView)

                    centerSubtitleView = MaterialTextView(context).apply {
                        setSingleLine()
                        ellipsize = TextUtils.TruncateAt.END
                        if (mSubtitleTextAppearance != Resources.ID_NULL) {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                setTextAppearance(mSubtitleTextAppearance)
                            } else setTextAppearance(context, mSubtitleTextAppearance)
                        }
                        if (mSubtitleTextColor != null) {
                            setTextColor(mSubtitleTextColor)
                        }
                        layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                    }
                    titleLayout.addView(centerSubtitleView)
                    addView(titleLayout)
                }
            }
            addView(centerTitleLayout)
        } else {
            setContentInsetsRelative(
                context.attrDimensionPixelSize(
                    R.attr.contentInsetStart,
                    dp2px(16f)
                ), 0
            )
            if (centerTitleLayout != null) {
                removeView(centerTitleLayout)
            }
        }
        logoDescription = logoDescription
        logo = logo
        title = title
        subtitle = subtitle
    }

    override fun setTitle(title: CharSequence?) {
        if (isCenterTitle) {
            super.setTitle(null)
            if (title.isNullOrEmpty()) {
                centerTitleView?.gone()
            } else {
                centerTitleView?.text = title
                centerTitleView?.visible()
            }
        } else super.setTitle(title)
    }

    @Suppress("DEPRECATION")
    override fun setTitleTextAppearance(context: Context?, resId: Int) {
        mTitleTextAppearance = resId
        super.setTitleTextAppearance(context, resId)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            centerTitleView?.setTextAppearance(mTitleTextAppearance)
        } else {
            centerTitleView?.setTextAppearance(context, mTitleTextAppearance)
        }
    }

    override fun setTitleTextColor(color: Int) {
        mTitleTextColor = ColorStateList.valueOf(color)
        super.setTitleTextColor(color)
        centerTitleView?.setTextColor(mTitleTextColor)
    }

    override fun setTitleTextColor(color: ColorStateList) {
        mTitleTextColor = color
        super.setTitleTextColor(color)
        centerTitleView?.setTextColor(mTitleTextColor)
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        if (isCenterTitle) {
            super.setSubtitle(null)
            if (subtitle.isNullOrEmpty()) {
                centerSubtitleView?.gone()
            } else {
                centerSubtitleView?.text = subtitle
                centerSubtitleView?.visible()
            }
        } else super.setSubtitle(subtitle)
    }

    override fun setSubtitleTextAppearance(context: Context?, resId: Int) {
        mSubtitleTextAppearance = resId
        super.setSubtitleTextAppearance(context, resId)
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            centerSubtitleView?.setTextAppearance(mSubtitleTextAppearance)
        } else {
            centerSubtitleView?.setTextAppearance(context, mSubtitleTextAppearance)
        }
    }

    override fun setSubtitleTextColor(color: Int) {
        mSubtitleTextColor = ColorStateList.valueOf(color)
        super.setSubtitleTextColor(color)
        centerSubtitleView?.setTextColor(mSubtitleTextColor)
    }

    override fun setSubtitleTextColor(color: ColorStateList) {
        mSubtitleTextColor = color
        super.setSubtitleTextColor(color)
        centerSubtitleView?.setTextColor(mSubtitleTextColor)
    }

    override fun setLogo(drawable: Drawable?) {

        if (isCenterTitle) {
            super.setLogo(null)
            if (drawable == null) {
                centerLogoView?.gone()
            } else {
                centerLogoView?.setImageDrawable(drawable)
                centerLogoView?.visible()
            }
        } else super.setLogo(drawable)
    }

    override fun setLogoDescription(description: CharSequence?) {
        centerLogoView?.contentDescription = description
        super.setLogoDescription(description)
    }
}