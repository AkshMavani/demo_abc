package com.example.demo_full


import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.abc.CustomHorizontalScrollView
import com.example.abc.CustomHorizontalScrollView.OnScrollViewListener


class MainActivity8 : AppCompatActivity() {
    lateinit var horizontalScrollView: CustomHorizontalScrollView
    lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main8)
        horizontalScrollView = findViewById<CustomHorizontalScrollView>(R.id.horizontalScrollView)
        linearLayout = findViewById<LinearLayout>(R.id.linearLayout)

        horizontalScrollView.setOnScrollViewListener(object : OnScrollViewListener {
            override fun onScrollChanged(
                scrollView: CustomHorizontalScrollView?,
                x: Int,
                y: Int,
                oldx: Int,
                oldy: Int
            ) {
                centerTextView()
            }
        })

        // Initially center the TextView and set the click listeners
        horizontalScrollView.post {
            centerTextView()
            setTextViewClickListeners()
        }
    }
    private fun centerTextView() {
        val scrollViewWidth = horizontalScrollView.width
        val scrollX = horizontalScrollView.scrollX
        val centerX = scrollX + scrollViewWidth / 2
        var closestTextView: TextView? = null
        var closestDistance = Int.MAX_VALUE

        for (i in 0 until linearLayout.childCount) {
            val view = linearLayout.getChildAt(i)
            if (view is TextView) {
                val textViewCenterX = view.left + view.width /3
                val distance = Math.abs(textViewCenterX - centerX)
                if (distance < closestDistance) {
                    closestDistance = distance
                    closestTextView = view
                }

                // Reset color to default for all TextViews
                view.setTextColor(Color.BLACK)
            }
        }

        // Set the closest TextView's color to yellow
        if (closestTextView?.text=="first"){
            Log.e("TAG1234", "centerTextView:1 ", )
        }
        if (closestTextView?.text=="second"){
            Log.e("TAG1234", "centerTextView:2 ", )
        }
        if (closestTextView?.text=="third"){
            Log.e("TAG1234", "centerTextView:3 ", )
        }

       // Toast.makeText(this, closestTextView?.text.toString(),Toast.LENGTH_SHORT).show()
        closestTextView?.setTextColor(Color.YELLOW)
    }

    private fun setTextViewClickListeners() {
        for (i in 0 until linearLayout.childCount) {
            val view = linearLayout.getChildAt(i)
            if (view is TextView) {
                view.setOnClickListener {
                    resetTextViewColors()
                    view.setTextColor(Color.YELLOW)
                   // Toast.makeText(this, "${(view as TextView).text} selected", Toast.LENGTH_SHORT).show()
                    horizontalScrollView.smoothScrollTo(view.left - (horizontalScrollView.width - view.width) / 2, 0)
                    Log.e("TAG1234", "setTextViewClickListeners:>>>${view.text} ", )
                    if (view?.text=="first"){
                        Log.e("TAG1234", "centerTextView:1>>>>> ", )
                    }
                    if (view?.text=="second"){
                        Log.e("TAG1234", "centerTextView:2>>>>>>>>> ", )
                    }
                    if (view?.text=="third"){
                        Log.e("TAG1234", "centerTextView:3 >>>>>>>>>>>>>>>>>>>>>", )
                    }
                }
            }
        }
    }

    private fun resetTextViewColors() {
        for (i in 0 until linearLayout.childCount) {
            val view = linearLayout.getChildAt(i)
            if (view is TextView) {
                view.setTextColor(Color.BLACK)
            }
        }
    }
}