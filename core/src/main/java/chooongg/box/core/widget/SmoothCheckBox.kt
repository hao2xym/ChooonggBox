package chooongg.box.core.widget

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Checkable
import chooongg.box.core.R
import chooongg.box.ext.attrColor
import chooongg.box.ext.dp2px
import chooongg.box.ext.resourcesColor
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sqrt

class SmoothCheckBox @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes), Checkable {

    private var mPaint: Paint? = null
    private var mTickPaint: Paint? = null
    private var mFloorPaint: Paint? = null
    private var mTickPoints: Array<Point>? = null
    private var mCenterPoint: Point? = null
    private var mTickPath: Path? = null
    private var mLeftLineDistance: Float = 0.toFloat()
    private var mRightLineDistance: Float = 0.toFloat()
    private var mDrewDistance: Float = 0.toFloat()
    private var mScaleVal = 1.0f
    private var mFloorScale = 1.0f
    private var mWidth: Int = 0
    private var mAnimDuration: Int = 0
    private var mStrokeWidth: Int = 0
    private var mCheckedColor: Int = 0
    private var mUnCheckedColor: Int = 0
    private var mFloorColor: Int = 0
    private var mFloorUnCheckedColor: Int = 0
    private var mChecked: Boolean = false
    private var mTickDrawing: Boolean = false
    private var mListener: OnCheckedChangeListener? = null

    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.SmoothCheckBox)
        val tickColor = ta.getColor(
            R.styleable.SmoothCheckBox_color_tick,
            context.attrColor(R.attr.colorOnSecondary, COLOR_TICK)
        )
        mAnimDuration = ta.getInt(R.styleable.SmoothCheckBox_duration, DEF_ANIM_DURATION)
        mFloorColor =
            ta.getColor(
                R.styleable.SmoothCheckBox_color_unchecked_stroke,
                context.attrColor(R.attr.colorControlNormal, COLOR_FLOOR_UNCHECKED)
            )
        mCheckedColor = ta.getColor(
            R.styleable.SmoothCheckBox_color_checked,
            context.attrColor(R.attr.colorPrimary, context.resourcesColor(R.color.color_secondary))
        )
        mUnCheckedColor = ta.getColor(R.styleable.SmoothCheckBox_color_unchecked, COLOR_UNCHECKED)
        mStrokeWidth = ta.getDimensionPixelSize(
            R.styleable.SmoothCheckBox_stroke_width,
            dp2px(0f)
        )
        ta.recycle()

        mFloorUnCheckedColor = mFloorColor
        mTickPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mTickPaint!!.style = Paint.Style.STROKE
        mTickPaint!!.strokeCap = Paint.Cap.ROUND
        mTickPaint!!.color = tickColor

        mFloorPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mFloorPaint!!.style = Paint.Style.FILL
        mFloorPaint!!.color = mFloorColor

        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mPaint!!.color = mCheckedColor

        mTickPath = Path()
        mCenterPoint = Point()
        mTickPoints = arrayOf(Point(), Point(), Point())

        setOnClickListener {
            toggle()
            mTickDrawing = false
            mDrewDistance = 0f
            if (isChecked) {
                startCheckedAnimation()
            } else {
                startUnCheckedAnimation()
            }
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val bundle = Bundle()
        bundle.putParcelable(KEY_INSTANCE_STATE, super.onSaveInstanceState())
        bundle.putBoolean(KEY_INSTANCE_STATE, isChecked)
        return bundle
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        if (state is Bundle) {
            val isChecked = state.getBoolean(KEY_INSTANCE_STATE)
            setChecked(isChecked)
            super.onRestoreInstanceState(state.getParcelable(KEY_INSTANCE_STATE))
            return
        }
        super.onRestoreInstanceState(state)
    }

    override fun isChecked(): Boolean {
        return mChecked
    }

    override fun toggle() {
        this.isChecked = !isChecked
    }

    override fun setChecked(checked: Boolean) {
        mChecked = checked
        reset()
        invalidate()
        if (mListener != null) {
            mListener!!.onCheckedChanged(this@SmoothCheckBox, mChecked)
        }
    }

    /**
     *
     * checked with animation
     * @param checked checked
     * @param animate change with animation
     */
    fun setChecked(checked: Boolean, animate: Boolean) {
        if (animate) {
            mTickDrawing = false
            mChecked = checked
            mDrewDistance = 0f
            if (checked) {
                startCheckedAnimation()
            } else {
                startUnCheckedAnimation()
            }
            if (mListener != null) {
                mListener!!.onCheckedChanged(this@SmoothCheckBox, mChecked)
            }

        } else {
            this.isChecked = checked
        }
    }

    private fun reset() {
        mTickDrawing = true
        mFloorScale = 1.0f
        mScaleVal = if (isChecked) 0f else 1.0f
        mFloorColor = if (isChecked) mCheckedColor else mFloorUnCheckedColor
        mDrewDistance = if (isChecked) mLeftLineDistance + mRightLineDistance else 0f
    }

    private fun measureSize(measureSpec: Int): Int {
        val defSize = dp2px(DEF_DRAW_SIZE.toFloat())
        val specSize = MeasureSpec.getSize(measureSpec)
        val specMode = MeasureSpec.getMode(measureSpec)

        var result = 0
        when (specMode) {
            MeasureSpec.UNSPECIFIED, MeasureSpec.AT_MOST -> result =
                min(defSize, specSize)
            MeasureSpec.EXACTLY -> result = specSize
        }
        return result
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(measureSize(widthMeasureSpec), measureSize(heightMeasureSpec))
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mWidth = measuredWidth
        mStrokeWidth = if (mStrokeWidth == 0) measuredWidth / 10 else mStrokeWidth
        mStrokeWidth = if (mStrokeWidth > measuredWidth / 5) measuredWidth / 5 else mStrokeWidth
        mStrokeWidth = if (mStrokeWidth < 3) 3 else mStrokeWidth
        mCenterPoint!!.x = mWidth / 2
        mCenterPoint!!.y = measuredHeight / 2

        mTickPoints!![0].x = round(measuredWidth.toFloat() / 30 * 7).toInt()
        mTickPoints!![0].y = round(measuredHeight.toFloat() / 30 * 14).toInt()
        mTickPoints!![1].x = round(measuredWidth.toFloat() / 30 * 13).toInt()
        mTickPoints!![1].y = round(measuredHeight.toFloat() / 30 * 20).toInt()
        mTickPoints!![2].x = round(measuredWidth.toFloat() / 30 * 22).toInt()
        mTickPoints!![2].y = round(measuredHeight.toFloat() / 30 * 10).toInt()

        mLeftLineDistance = sqrt(
            (mTickPoints!![1].x - mTickPoints!![0].x).toDouble().pow(2.0)
                    + (mTickPoints!![1].y - mTickPoints!![0].y).toDouble().pow(2.0)
        ).toFloat()
        mRightLineDistance = sqrt(
            (mTickPoints!![2].x - mTickPoints!![1].x).toDouble().pow(2.0)
                    + (mTickPoints!![2].y - mTickPoints!![1].y).toDouble().pow(2.0)
        ).toFloat()
        mTickPaint!!.strokeWidth = mStrokeWidth.toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        drawBorder(canvas)
        drawCenter(canvas)
        drawTick(canvas)
    }

    private fun drawCenter(canvas: Canvas) {
        mPaint!!.color = mUnCheckedColor
        val radius = (mCenterPoint!!.x - mStrokeWidth) * mScaleVal
        canvas.drawCircle(mCenterPoint!!.x.toFloat(), mCenterPoint!!.y.toFloat(), radius, mPaint!!)
    }

    private fun drawBorder(canvas: Canvas) {
        mFloorPaint!!.color = mFloorColor
        val radius = mCenterPoint!!.x
        canvas.drawCircle(
            mCenterPoint!!.x.toFloat(),
            mCenterPoint!!.y.toFloat(),
            radius * mFloorScale,
            mFloorPaint!!
        )
    }

    private fun drawTick(canvas: Canvas) {
        if (mTickDrawing && isChecked) {
            drawTickPath(canvas)
        }
    }

    private fun drawTickPath(canvas: Canvas) {
        mTickPath!!.reset()
        // draw left of the tick
        if (mDrewDistance < mLeftLineDistance) {
            val step = if (mWidth / 20.0f < 3) 3f else mWidth / 20.0f
            mDrewDistance += step
            val stopX =
                mTickPoints!![0].x + (mTickPoints!![1].x - mTickPoints!![0].x) * mDrewDistance / mLeftLineDistance
            val stopY =
                mTickPoints!![0].y + (mTickPoints!![1].y - mTickPoints!![0].y) * mDrewDistance / mLeftLineDistance

            mTickPath!!.moveTo(mTickPoints!![0].x.toFloat(), mTickPoints!![0].y.toFloat())
            mTickPath!!.lineTo(stopX, stopY)
            canvas.drawPath(mTickPath!!, mTickPaint!!)

            if (mDrewDistance > mLeftLineDistance) {
                mDrewDistance = mLeftLineDistance
            }
        } else {

            mTickPath!!.moveTo(mTickPoints!![0].x.toFloat(), mTickPoints!![0].y.toFloat())
            mTickPath!!.lineTo(mTickPoints!![1].x.toFloat(), mTickPoints!![1].y.toFloat())
            canvas.drawPath(mTickPath!!, mTickPaint!!)

            // draw right of the tick
            if (mDrewDistance < mLeftLineDistance + mRightLineDistance) {
                val stopX =
                    mTickPoints!![1].x + (mTickPoints!![2].x - mTickPoints!![1].x) * (mDrewDistance - mLeftLineDistance) / mRightLineDistance
                val stopY =
                    mTickPoints!![1].y - (mTickPoints!![1].y - mTickPoints!![2].y) * (mDrewDistance - mLeftLineDistance) / mRightLineDistance

                mTickPath!!.reset()
                mTickPath!!.moveTo(mTickPoints!![1].x.toFloat(), mTickPoints!![1].y.toFloat())
                mTickPath!!.lineTo(stopX, stopY)
                canvas.drawPath(mTickPath!!, mTickPaint!!)

                val step = (if (mWidth / 20 < 3) 3 else mWidth / 20).toFloat()
                mDrewDistance += step
            } else {
                mTickPath!!.reset()
                mTickPath!!.moveTo(mTickPoints!![1].x.toFloat(), mTickPoints!![1].y.toFloat())
                mTickPath!!.lineTo(mTickPoints!![2].x.toFloat(), mTickPoints!![2].y.toFloat())
                canvas.drawPath(mTickPath!!, mTickPaint!!)
            }
        }

        // invalidate
        if (mDrewDistance < mLeftLineDistance + mRightLineDistance) {
            postDelayed({ postInvalidate() }, 10)
        }
    }

    private fun startCheckedAnimation() {
        val animator = ValueAnimator.ofFloat(1.0f, 0f)
        animator.duration = (mAnimDuration / 3 * 2).toLong()
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            mScaleVal = animation.animatedValue as Float
            mFloorColor = getGradientColor(mUnCheckedColor, mCheckedColor, 1 - mScaleVal)
            postInvalidate()
        }
        animator.start()

        val floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f)
        floorAnimator.duration = mAnimDuration.toLong()
        floorAnimator.interpolator = LinearInterpolator()
        floorAnimator.addUpdateListener { animation ->
            mFloorScale = animation.animatedValue as Float
            postInvalidate()
        }
        floorAnimator.start()

        drawTickDelayed()
    }

    private fun startUnCheckedAnimation() {
        val animator = ValueAnimator.ofFloat(0f, 1.0f)
        animator.duration = mAnimDuration.toLong()
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animation ->
            mScaleVal = animation.animatedValue as Float
            mFloorColor = getGradientColor(mCheckedColor, COLOR_FLOOR_UNCHECKED, mScaleVal)
            postInvalidate()
        }
        animator.start()

        val floorAnimator = ValueAnimator.ofFloat(1.0f, 0.8f, 1.0f)
        floorAnimator.duration = mAnimDuration.toLong()
        floorAnimator.interpolator = LinearInterpolator()
        floorAnimator.addUpdateListener { animation ->
            mFloorScale = animation.animatedValue as Float
            postInvalidate()
        }
        floorAnimator.start()
    }

    private fun drawTickDelayed() {
        postDelayed({
            mTickDrawing = true
            postInvalidate()
        }, mAnimDuration.toLong())
    }

    fun setOnCheckedChangeListener(l: OnCheckedChangeListener?) {
        this.mListener = l
    }

    interface OnCheckedChangeListener {
        fun onCheckedChanged(checkBox: SmoothCheckBox, isChecked: Boolean)
    }

    companion object {
        private const val KEY_INSTANCE_STATE = "InstanceState"
        private const val COLOR_TICK = Color.WHITE
        private const val COLOR_UNCHECKED = Color.TRANSPARENT
        private const val COLOR_FLOOR_UNCHECKED = 0xDFDFDF
        private const val DEF_DRAW_SIZE = 24
        private const val DEF_ANIM_DURATION = 300

        private fun getGradientColor(startColor: Int, endColor: Int, percent: Float): Int {
            val sr = startColor and 0xff0000 shr 0x10
            val sg = startColor and 0xff00 shr 0x8
            val sb = startColor and 0xff

            val er = endColor and 0xff0000 shr 0x10
            val eg = endColor and 0xff00 shr 0x8
            val eb = endColor and 0xff

            val cr = (sr * (1 - percent) + er * percent).toInt()
            val cg = (sg * (1 - percent) + eg * percent).toInt()
            val cb = (sb * (1 - percent) + eb * percent).toInt()
            return Color.argb(0xff, cr, cg, cb)
        }
    }
}