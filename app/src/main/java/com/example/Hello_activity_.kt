package com.example

import android.graphics.Color
import android.os.Bundle
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R


class Hello_activity_ : AppCompatActivity() {
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var linearLayout: LinearLayout
    private lateinit var textViews: List<TextView>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello)
        horizontalScrollView = findViewById(R.id.scroll123)
        linearLayout = findViewById(R.id.ll12)

        textViews = listOf(
            findViewById(R.id.text11),
            findViewById(R.id.text21),
            findViewById(R.id.text31),
            findViewById(R.id.text41),
            findViewById(R.id.text51)
        )

        // Scroll to the default selected item (3rd item)
        horizontalScrollView.post {
            scrollToPosition(2) // Position index starts at 0, so 2 is the 3rd item
        }

        horizontalScrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            onScrollChanged()
        }
    }

    private fun scrollToPosition(position: Int) {
        val itemWidth = textViews[0].width
        val scrollX = (position * itemWidth) - (horizontalScrollView.width / 2) + (itemWidth / 2)
        horizontalScrollView.smoothScrollTo(scrollX, 0)
        updateTextColors(position)
    }

    private fun onScrollChanged() {
        val itemWidth = textViews[0].width
        val scrollX = horizontalScrollView.scrollX + horizontalScrollView.width / 2
        val selectedItemPosition = (scrollX / itemWidth).toInt()

        updateTextColors(selectedItemPosition)
    }

    private fun updateTextColors(selectedPosition: Int) {
        for (i in textViews.indices) {
            if (i == selectedPosition) {
                textViews[i].setTextColor(Color.YELLOW)
            } else {
                textViews[i].setTextColor(Color.BLACK)
            }
        }
    }
}