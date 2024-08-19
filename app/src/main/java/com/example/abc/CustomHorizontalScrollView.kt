package com.example.abc

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView


class CustomHorizontalScrollView : HorizontalScrollView {
    interface OnScrollViewListener {
        fun onScrollChanged(
            scrollView: CustomHorizontalScrollView?,
            x: Int,
            y: Int,
            oldx: Int,
            oldy: Int,
        )
    }

    private var onScrollViewListener: OnScrollViewListener? = null

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}

    fun setOnScrollViewListener(listener: OnScrollViewListener?) {
        onScrollViewListener = listener
    }

    override fun onScrollChanged(x: Int, y: Int, oldx: Int, oldy: Int) {
        super.onScrollChanged(x, y, oldx, oldy)
        if (onScrollViewListener != null) {
            onScrollViewListener!!.onScrollChanged(this, x, y, oldx, oldy)
        }
    }
}
