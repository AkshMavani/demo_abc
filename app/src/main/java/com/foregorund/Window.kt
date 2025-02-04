package com.foregorund

import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2

import com.example.demo_full.R
import com.foregorund.adapter.MyForegroundService
import com.foregorund.adapter.SliderAdapter
import com.foregorund.adapter.interface_click
import com.foregorund.adapter.slider

class Window(
    // declaring required variables
    private val context: Context,
) {
    private val mView: View
    private var mParams: WindowManager.LayoutParams? = null
    private val mWindowManager: WindowManager
    private val layoutInflater: LayoutInflater
    private val cl: ConstraintLayout
    private val pager: ViewPager2
    private val img: ImageView
    private val ll: LinearLayout



    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // set the layout parameters of the window
            mParams = WindowManager.LayoutParams( // Shrink the window to wrap the content rather
                // than filling the screen
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS or
                        WindowManager.LayoutParams.FLAG_FULLSCREEN,
                PixelFormat.TRANSLUCENT
            )
        }
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = layoutInflater.inflate(R.layout.popup_window, null)
        mView.findViewById<View>(R.id.window_close).setOnClickListener { close() }
        cl=mView.findViewById<ConstraintLayout>(R.id.cl)
        img=mView.findViewById<ImageView>(R.id.img)
        ll=mView.findViewById<LinearLayout>(R.id.ll)
        pager=mView.findViewById(R.id.pager)

        cl.setOnLongClickListener {
            pager.visibility= View.VISIBLE
            mView.findViewById<View>(R.id.window_close).visibility= View.GONE
            val sliderItems: MutableList<slider> = ArrayList<slider>()
            sliderItems.add(slider(R.drawable.abc))
            sliderItems.add(slider(R.drawable.def))
            sliderItems.add(slider(R.drawable.jk))
            sliderItems.add(slider(R.drawable.kl))
            sliderItems.add(slider(R.drawable.op))
            var click=object : interface_click {
                override fun click_item(s: Int) {
                    pager.visibility= View.GONE
                    mView.findViewById<View>(R.id.window_close).visibility= View.VISIBLE
                    img.setImageResource(s)
                }

            }
            pager.setAdapter(SliderAdapter(sliderItems, pager,context,click))
            pager.setClipToPadding(false);
            pager.setClipChildren(false);
            pager.setOffscreenPageLimit(3);
            pager.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER)
            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer( MarginPageTransformer(40))
            compositePageTransformer.addTransformer(ViewPager2.PageTransformer { page, position ->
                val r = 1 - kotlin.math.abs(position)
                page.scaleY = 0.85f + r * 0.15f
            })
            pager.setPageTransformer(compositePageTransformer)
            true

        }
//        val sp=context.getSharedPreferences("SP", MODE_PRIVATE)
//        val img=  sp.getInt("img",0)



        mParams!!.gravity = Gravity.CENTER
        mWindowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    }

    fun open() {

        try {
            // check if the view is already
            // inflated or present in the window
            if (mView.windowToken == null) {
                if (mView.parent == null) {
                    mWindowManager.addView(mView, mParams)
                }
            }
        } catch (e: Exception) {
            Log.e("Error1", e.toString())
        }
    }

    fun close() {
        try {
            // Notify service that the window is closed
            val intent = Intent(context, MyService::class.java).apply {
                putExtra("window_closed", true)
            }
            context.startService(intent)

            // Remove the window view
            mWindowManager.removeView(mView)
            mView.invalidate()
            (mView.parent as ViewGroup).removeAllViews()
        } catch (e: Exception) {
            Log.d("Error", e.toString())
        }
    }



}