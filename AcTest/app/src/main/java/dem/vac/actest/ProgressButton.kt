package dem.vac.actest


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


/**
 * 作者：Vaccae
 * 邮箱：3657447@qq.com
 * 创建时间：2019-12-06 15:48
 * 功能模块说明：
 */

class ProgressButton : View {

    constructor(context: Context?, attrs: AttributeSet?) : super( context, attrs){
        init(context!!, attrs!!)
    }


    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs){
        init(context!!, attrs!!)
    }


    private lateinit var fm: Paint.FontMetrics
    private var progress = 0
    private var textColor: Int = Color.WHITE
    private var paint: Paint? = null
    private var textSize: Float = 10f
    private var foreground = 0
    private var backgroundcolor = 0
    private var text: String? = null
    private var max = 100
    private val corner = 5 // 圆角的弧度
    private var buttonClickListener: OnProgressButtonClickListener? = null


    private fun init(
        context: Context,
        attrs: AttributeSet
    ) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ProgressButton)
        backgroundcolor = typedArray.getInteger(
            R.styleable.ProgressButton_backgroundcolor,
            Color.parseColor("#C6C6C6")
        )
        foreground = typedArray.getInteger(
            R.styleable.ProgressButton_foreground,
            Color.rgb(20, 131, 214)
        )
        textColor = typedArray.getInteger(
            R.styleable.ProgressButton_textcolor,
            Color.WHITE
        )
        max = typedArray.getInteger(R.styleable.ProgressButton_max, 100)
        progress = typedArray.getInteger(R.styleable.ProgressButton_progress, 0)
        text = typedArray.getString(R.styleable.ProgressButton_text)
        textSize = typedArray.getDimension(R.styleable.ProgressButton_textSize, 20f)
        typedArray.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint = Paint()
        paint!!.setAntiAlias(true)
        paint!!.setStrokeWidth(5f)
        /**
         * 绘制背景
         */
        var oval = RectF(0F, 0F, width.toFloat(), height.toFloat())
        paint!!.setColor(this.backgroundcolor)
        canvas.drawRoundRect(oval, corner.toFloat(), corner.toFloat(), paint)
        /***
         * 绘制进度值
         */
        paint!!.setColor(foreground)
        if (progress <= corner) {
            oval = RectF(
                0F,
                (corner - progress).toFloat(),
                (width * progress / max).toFloat(),
                (height
                        - corner + progress).toFloat()
            )
            canvas.drawRoundRect(oval, progress.toFloat(), progress.toFloat(), paint)
        } else {
            oval = RectF(
                0F, 0F,
                (width * progress / max).toFloat(),
                height.toFloat()
            )
            canvas.drawRoundRect(oval, corner.toFloat(), corner.toFloat(), paint)
        }
        /***
         * 绘制文本
         */
        if ("" == text || text == null) {
            return
        }
        paint!!.setTextSize(textSize)
        fm = paint!!.getFontMetrics()
        paint!!.setColor(textColor)
        val textCenterVerticalBaselineY =
            height / 2 - fm.descent + (fm.descent - fm.ascent) / 2
        canvas.drawText(
            text,
            (measuredWidth - paint!!.measureText(text)) / 2,
            textCenterVerticalBaselineY,
            paint
        )
    }

    /**
     * 设置最大值
     *
     * @param max
     */
    fun setMax(max: Int) {
        this.max = max
    }

    /**
     * 设置文本提示信息
     *
     * @param text
     */
    fun setText(text: String?) {
        this.text = text
    }

    /**
     * 设置进度条的颜色值
     *
     * @param color
     */
    fun setForeground(color: Int) {
        foreground = color
    }

    /**
     * 设置进度条的背景色
     */
    override fun setBackgroundColor(color: Int) {
        this.backgroundcolor = color
    }

    /***
     * 设置文本的大小
     */
    fun setTextSize(size: Int) {
        textSize = size.toFloat()
    }

    /**
     * 设置文本的颜色值
     *
     * @param color
     */
    fun setTextColor(color: Int) {
        textColor = color
    }

    /**
     * 设置进度值
     *
     * @param progress
     */
    fun setProgress(progress: Int) {
        if (progress > max) {
            return
        }
        this.progress = progress
        //设置进度之后，要求UI强制进行重绘
        postInvalidate()
    }

    fun getMax(): Int {
        return max
    }

    fun getProgress(): Int {
        return progress
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_UP -> buttonClickListener!!.onClickListener()
            else -> {
            }
        }
        return true
    }

    fun setOnProgressButtonClickListener(clickListener: OnProgressButtonClickListener) {
        buttonClickListener = clickListener
    }


    interface OnProgressButtonClickListener {
        fun onClickListener()
    }

}