package chooongg.box.core.widget

import android.R
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import chooongg.box.core.widget.PinEntryEditText.OnPinEnteredListener
import chooongg.box.ext.dp2px
import com.google.android.material.textfield.TextInputEditText

class PinEntryEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : TextInputEditText(context, attrs, defStyle) {

    companion object {
        const val DEFAULT_MASK = "\u25CF"
    }

    protected var mMask: String? = null
    protected var mMaskChars: StringBuilder? = null
    protected var mSingleCharHint: String? = null
    protected var mAnimatedType = 0
    protected var mSpace = 24f //24 dp by default, space between the lines

    protected var mCharSize = 0f
    protected var mNumChars = 4f
    protected var mTextBottomPadding = 8f //8dp by default, height of the text from our lines

    protected var mMaxLength = 4
    protected var mLineCoords: Array<RectF>
    protected var mCharBottom: FloatArray
    protected var mCharPaint: Paint? = null
    protected var mLastCharPaint: Paint? = null
    protected var mSingleCharPaint: Paint? = null
    protected var mPinBackground: Drawable? = null
    protected var mTextHeight = Rect()
    protected var mIsDigitSquare = false

    protected var mClickListener: View.OnClickListener? = null
    protected var mOnPinEnteredListener: OnPinEnteredListener? = null

    protected var mLineStroke = dp2px(1f).toFloat() //1dp by default
    protected var mLineStrokeSelected = dp2px(2f).toFloat() //2dp by default

    protected var mLinesPaint: Paint? = null
    protected var mAnimate = false
    protected var mHasError = false
    protected var mOriginalTextColors: ColorStateList? = null
    protected var mStates = arrayOf(
        intArrayOf(R.attr.state_selected), intArrayOf(R.attr.state_active), intArrayOf(
            R.attr.state_focused
        ), intArrayOf(-R.attr.state_focused)
    )

    protected var mColors = intArrayOf(
        Color.GREEN,
        Color.RED,
        Color.BLACK,
        Color.GRAY
    )

    protected var mColorStates = ColorStateList(mStates, mColors)

    init {

    }
}