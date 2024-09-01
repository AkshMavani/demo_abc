package com.example.wheel

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewGroup
import kotlin.math.abs

class CustomHorizontalWheelView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var itemMargin = 8f // Default margin in pixels

    var selectedItemPosition = 2 // Set to 2 for the 3rd item as default
        private set
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 48f
    }

    private var startX = 0f
    private var lastX = 0f
    private var offsetX = 0f
    private var itemWidth = 0f

    private var items: List<String> = emptyList()

    private var padding = 16f // Default padding in pixels

    var onItemSelectedListener: ((Int) -> Unit)? = null

    init {
        setWillNotDraw(false)
    }

    fun setItems(items: List<String>) {
        this.items = items
        requestLayout()
        invalidate()
    }

    fun setSelectedItemPosition(position: Int) {
        selectedItemPosition = position
        offsetX = 0f
        requestLayout()
        invalidate()
        onItemSelectedListener?.invoke(position)
    }

    fun setItemPadding(paddingInPixels: Float) {
        padding = paddingInPixels
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        itemWidth = width.toFloat() / 5  // Adjust to show 5 items at a time

        setMeasuredDimension(width, height)
    }

    override fun onLayout(p0: Boolean, p1: Int, p2: Int, p3: Int, p4: Int) {
        // No children to layout
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f

        for (i in items.indices) {
            val text = items[i]
            val x = centerX + (i - selectedItemPosition) * (itemWidth + itemMargin) + offsetX
            val y = height / 2f

            paint.color = if (i == selectedItemPosition) Color.YELLOW else Color.BLACK

            // Apply padding to the text
            canvas.drawText(text, x - paint.measureText(text) / 2 + padding, y + padding, paint)
        }
    }


    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                lastX = startX
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.x - lastX
                offsetX += dx
                lastX = event.x
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                val totalScrollDistance = event.x - startX
                val dx = totalScrollDistance / (itemWidth + itemMargin)

                if (abs(totalScrollDistance) < 10) {
                    // Detect click by checking if the movement was very small
                    val clickedIndex = calculateClickedItemIndex(event.x)
                    setSelectedItemPosition(clickedIndex)
                } else if (abs(dx) > 0.5f) {
                    selectedItemPosition += if (dx > 0) -1 else 1
                    selectedItemPosition = selectedItemPosition.coerceIn(0, items.size - 1)
                }

                offsetX = 0f

                invalidate()
                onItemSelectedListener?.invoke(selectedItemPosition)
                return true
            }
        }
        return super.onTouchEvent(event)
    }


    private fun calculateClickedItemIndex(x: Float): Int {
        val centerX = width / 2f
        val clickedIndex = ((x - centerX + selectedItemPosition * (itemWidth + itemMargin)) / (itemWidth + itemMargin)).toInt()
        return clickedIndex.coerceIn(0, items.size - 1)
    }

}