package chooongg.box.core.widget

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.widget.FrameLayout
import chooongg.box.core.R
import chooongg.box.ext.attrBoolean
import com.google.android.material.appbar.MaterialToolbar

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

    private var centerTitleBar: FrameLayout? = null

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
    }

    override fun setTitle(resId: Int) {
        super.setTitle(resId)
    }

    override fun setTitle(title: CharSequence?) {
        super.setTitle(title)
    }

    override fun setTitleTextAppearance(context: Context?, resId: Int) {
        super.setTitleTextAppearance(context, resId)
    }

    override fun setTitleTextColor(color: Int) {
        mTitleTextColor = ColorStateList.valueOf(color)
        super.setTitleTextColor(color)
    }

    override fun setTitleTextColor(color: ColorStateList) {
        mTitleTextColor = color
        super.setTitleTextColor(color)
    }

    override fun setSubtitle(resId: Int) {
        super.setSubtitle(resId)
    }

    override fun setSubtitle(subtitle: CharSequence?) {
        super.setSubtitle(subtitle)
    }

    override fun setSubtitleTextAppearance(context: Context?, resId: Int) {
        super.setSubtitleTextAppearance(context, resId)
    }

    override fun setSubtitleTextColor(color: Int) {
        mSubtitleTextColor = ColorStateList.valueOf(color)
        super.setSubtitleTextColor(color)
    }

    override fun setSubtitleTextColor(color: ColorStateList) {
        mSubtitleTextColor = color
        super.setSubtitleTextColor(color)
    }
}