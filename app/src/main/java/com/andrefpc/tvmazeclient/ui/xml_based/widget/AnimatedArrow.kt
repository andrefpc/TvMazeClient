package com.andrefpc.tvmazeclient.ui.xml_based.widget

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.Path
import android.graphics.Point
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.FloatRange
import androidx.annotation.IntDef
import com.andrefpc.tvmazeclient.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

class AnimatedArrow(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attrs, defStyleAttr, defStyleRes) {
    constructor(context: Context) : this(context, null, 0, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0, 0)

    companion object {

        const val MORE = 0
        const val LESS = 1
        const val INTERMEDIATE = 2
        const val MORE_STATE_ALPHA = -45f
        const val LESS_STATE_ALPHA = 45f
        const val DELTA_ALPHA = 90f
        const val THICKNESS_PROPORTION = 5f / 36f
        const val PADDING_PROPORTION = 4f / 24f
        const val DEFAULT_ANIMATION_DURATION: Long = 150
    }

    @IntDef(MORE, LESS, INTERMEDIATE)
    annotation class State

    @State
    private var state = 0
    private var mAlpha = MORE_STATE_ALPHA
    private var centerTranslation = 0f

    @FloatRange(from = 0.0, to = 1.0)
    private var fraction = 0f
    private var animationSpeed = 0f

    private var switchColor = false
    private var color: Int = Color.BLACK
    private var colorMore = 0
    private var colorLess = 0
    private var colorIntermediate = 0

    private var paint: Paint? = null
    private val left: Point = Point()
    private val right: Point = Point()
    private val center: Point = Point()
    private val tempLeft: Point = Point()
    private val tempRight: Point = Point()

    private var useDefaultPadding = false
    private var padding = 0

    private val path: Path = Path()

    private var arrowAnimator: ValueAnimator? = null

    init {
        attrs?.let { init(it) }
    }

    fun init(attrs: AttributeSet) {
        val array = context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.AnimatedArrow,
            0,
            0
        )

        val roundedCorners: Boolean
        val animationDuration: Long
        try {
            roundedCorners = array.getBoolean(
                R.styleable.AnimatedArrow_animated_arrow_rounded_corners,
                false
            )
            switchColor = array.getBoolean(
                R.styleable.AnimatedArrow_animated_arrow_switch_color,
                false
            )
            color =
                array.getColor(R.styleable.AnimatedArrow_animated_arrow_color, Color.BLACK)
            colorMore = array.getColor(
                R.styleable.AnimatedArrow_animated_arrow_color_more,
                Color.BLACK
            )
            colorLess = array.getColor(
                R.styleable.AnimatedArrow_animated_arrow_color_less,
                Color.BLACK
            )
            colorIntermediate = array.getColor(
                R.styleable.AnimatedArrow_animated_arrow_color_intermediate,
                -1
            )
            animationDuration = array.getInteger(
                R.styleable.AnimatedArrow_animated_arrow_animation_duration,
                DEFAULT_ANIMATION_DURATION.toInt()
            ).toLong()
            padding = array.getDimensionPixelSize(
                R.styleable.AnimatedArrow_animated_arrow_padding,
                -1
            )
            useDefaultPadding = padding == -1
        } finally {
            array.recycle()
        }

        paint = Paint(ANTI_ALIAS_FLAG)
        paint?.let { paint ->
            paint.color = color
            paint.style = Paint.Style.STROKE
            paint.isDither = true
            if (roundedCorners) {
                paint.strokeJoin = Paint.Join.ROUND
                paint.strokeCap = Paint.Cap.ROUND
            }
        }

        animationSpeed = DELTA_ALPHA / animationDuration
        setState(MORE, false)
    }

    fun switchState() {
        switchState(true)
    }

    /**
     * Changes state and updates view
     *
     * @param animate Indicates thaw state will be changed with animation or not
     */
    private fun switchState(animate: Boolean) {
        val newState: Int = when (state) {
            MORE -> LESS
            LESS -> MORE
            INTERMEDIATE -> getFinalStateByFraction()
            else -> throw IllegalArgumentException("Unknown state [$state]")
        }
        setState(newState, animate)
    }

    /**
     * Set one of two states and updates view
     *
     * @param state   [.MORE] or [.LESS]
     * @param animate Indicates thaw state will be changed with animation or not
     * @throws IllegalArgumentException if {@param state} is invalid
     */
    fun setState(@State state: Int, animate: Boolean) {
        this.state = state
        fraction = when (state) {
            MORE -> {
                0f
            }
            LESS -> {
                1f
            }
            else -> {
                throw IllegalArgumentException("Unknown state, must be one of STATE_MORE = 0,  STATE_LESS = 1")
            }
        }
        updateArrow(animate)
    }

    /**
     * Set current fraction for arrow and updates view
     *
     * @param fraction Must be value from 0f to 1f [.MORE] state value is 0f, [.LESS]
     * state value is 1f
     * @throws IllegalArgumentException if fraction is less than 0f or more than 1f
     */
    fun setFraction(@FloatRange(from = 0.0, to = 1.0) fraction: Float, animate: Boolean) {
        if (fraction < 0.0 || fraction > 1.0) return
        require(!(fraction < 0f || fraction > 1f)) { "Fraction value must be from 0 to 1f, fraction=$fraction" }
        if (this.fraction == fraction) {
            return
        }
        this.fraction = fraction
        state = when (fraction) {
            0f -> {
                MORE
            }
            1f -> {
                LESS
            }
            else -> {
                INTERMEDIATE
            }
        }
        updateArrow(animate)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.translate(0F, centerTranslation)
        paint?.let { canvas.drawPath(path, it) }
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        calculateArrowMetrics(width, height)
        updateArrowPath()
    }

    private fun calculateArrowMetrics(width: Int, height: Int) {
        val arrowMaxWidth = if (height >= width) width else height
        if (useDefaultPadding) {
            padding = (PADDING_PROPORTION * arrowMaxWidth).toInt()
        }
        val arrowWidth = arrowMaxWidth - 2 * padding
        val thickness: Float = (arrowWidth * THICKNESS_PROPORTION)
        paint?.strokeWidth = thickness
        center.set(width / 2, height / 2)
        left.set(center.x - arrowWidth / 2, center.y)
        right.set(center.x + arrowWidth / 2, center.y)
    }

    private fun updateArrow(animate: Boolean) {
        val toAlpha = MORE_STATE_ALPHA + fraction * DELTA_ALPHA
        if (animate) {
            animateArrow(toAlpha)
        } else {
            cancelAnimation()
            mAlpha = toAlpha
            if (switchColor) {
                updateColor(ArgbEvaluator())
            }
            updateArrowPath()
            invalidate()
        }
    }

    private fun updateArrowPath() {
        path.reset()
        rotate(left, -mAlpha.toDouble(), tempLeft)
        rotate(right, mAlpha.toDouble(), tempRight)
        centerTranslation = ((center.y - tempLeft.y) / 2).toFloat()
        path.moveTo(tempLeft.x.toFloat(), tempLeft.y.toFloat())
        path.lineTo(center.x.toFloat(), center.y.toFloat())
        path.lineTo(tempRight.x.toFloat(), tempRight.y.toFloat())
    }

    private fun animateArrow(toAlpha: Float) {
        cancelAnimation()
        val valueAnimator = ValueAnimator.ofFloat(mAlpha, toAlpha)
        valueAnimator.addUpdateListener(
            object : AnimatorUpdateListener {
                private val colorEvaluator: ArgbEvaluator = ArgbEvaluator()
                override fun onAnimationUpdate(valueAnimator: ValueAnimator) {
                    mAlpha = valueAnimator.animatedValue as Float
                    updateArrowPath()
                    if (switchColor) {
                        updateColor(colorEvaluator)
                    }
                    postInvalidateOnAnimationCompat()
                }
            }
        )
        valueAnimator.interpolator = DecelerateInterpolator()
        valueAnimator.duration = calculateAnimationDuration(toAlpha)
        valueAnimator.start()
        arrowAnimator = valueAnimator
    }

    private fun cancelAnimation() {
        if (arrowAnimator != null && arrowAnimator?.isRunning == true) {
            arrowAnimator?.cancel()
        }
    }

    private fun updateColor(colorEvaluator: ArgbEvaluator) {
        val fraction: Float
        val colorFrom: Int
        val colorTo: Int
        if (colorIntermediate != -1) {
            colorFrom = if (mAlpha <= 0f) colorMore else colorIntermediate
            colorTo = if (mAlpha <= 0f) colorIntermediate else colorLess
            fraction = if (mAlpha <= 0) 1 + mAlpha / 45f else mAlpha / 45f
        } else {
            colorFrom = colorMore
            colorTo = colorLess
            fraction = (mAlpha + 45f) / 90f
        }
        color = colorEvaluator.evaluate(fraction, colorFrom, colorTo) as Int
        paint?.color = color
    }

    private fun calculateAnimationDuration(toAlpha: Float): Long {
        return (abs(toAlpha - mAlpha) / animationSpeed).toLong()
    }

    private fun rotate(startPosition: Point, degrees: Double, target: Point) {
        val angle = Math.toRadians(degrees)
        val x = (
            center.x + (startPosition.x - center.x) * cos(angle) -
                (startPosition.y - center.y) * sin(angle)
            ).toInt()
        val y =
            (
                center.y + (startPosition.x - center.x) * sin(angle) + (startPosition.y - center.y) * cos(
                    angle
                )
                ).toInt()
        target.set(x, y)
    }

    @State
    private fun getFinalStateByFraction(): Int {
        return if (fraction <= .5f) {
            MORE
        } else {
            LESS
        }
    }

    private fun postInvalidateOnAnimationCompat() {
        postInvalidateOnAnimation()
    }
}
