package com.udacity

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var textColor: Int = 0
    private var backgroundButtonColor: Int = 0
    private var backgroundAnimatedColor: Int = 0
    private var animatedCircleColor: Int = 0

    private var animate = false
    private var animatedWidth: Int = 0
    private var animatedAngle: Float = 0f

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.NORMAL)
    }

    private val valueAnimator = ValueAnimator()

    internal var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Loading -> {
                // start animation
                animate = true
                invalidate()
            }
            ButtonState.Completed -> {
                // stop animation
                animate = false
                // reset the animation values to zero
                animatedWidth = 0
                animatedAngle = 0f
            }
        }
    }


    init {
        buttonState = ButtonState.Completed
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            textColor = getColor(R.styleable.LoadingButton_textColor, 0)
            backgroundButtonColor = getColor(R.styleable.LoadingButton_backgroundColor, 0)
            backgroundAnimatedColor = getColor(R.styleable.LoadingButton_backgroundAnimatedColor, 0)
            animatedCircleColor = getColor(R.styleable.LoadingButton_animatedCircleColor, 0)
        }
    }

    override fun performClick(): Boolean {
        if (super.performClick()) return true

        invalidate()
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Draw the rectangle
        paint.color = backgroundButtonColor
        canvas?.drawRect(0.0f, 0.0f, widthSize.toFloat(), heightSize.toFloat(), paint)

        // if need to animate
        if (animate) {
            // Draw the "loading bar"
            if (animatedWidth < widthSize) {
                animatedWidth += 5
            } else {
                animatedWidth = 1
            }
            paint.color = backgroundAnimatedColor
            canvas?.drawRect(0.0f, 0.0f, animatedWidth.toFloat(), heightSize.toFloat(), paint)

            // Draw the downloading text
            paint.color = textColor
            // Vertically center the text itself instead of the baseline
            val textHeight = paint.descent() - paint.ascent()
            val textOffset = (textHeight / 2) - paint.descent()
            canvas?.drawText(
                    resources.getString(R.string.button_loading),
                    widthSize.toFloat()/2,
                    heightSize.toFloat()/2 + textOffset,
                    paint
            )

            // Draw the "loading circle"
            if (animatedAngle < 360f) {
                animatedAngle += 5f
            } else {
                animatedAngle = 5f
            }
            paint.color = animatedCircleColor
            canvas?.drawArc(
                    (3f/4f)*widthSize,
                    1f/4f*heightSize,
                    (3f/4f)*widthSize + (1f/10f)*widthSize,
                    3f/4f*heightSize,
                    0f,
                    animatedAngle,
                    true,
                    paint
            )

            invalidate()
        } else {
            // Draw the "normal" non-downloading text
            paint.color = textColor
            // Vertically center the text itself instead of the baseline
            val textHeight = paint.descent() - paint.ascent()
            val textOffset = (textHeight / 2) - paint.descent()
            canvas?.drawText(
                    resources.getString(R.string.download),
                    widthSize.toFloat()/2,
                    heightSize.toFloat()/2 + textOffset,
                    paint
            )
        }

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}