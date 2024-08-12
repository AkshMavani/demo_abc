package com.example.multifit

import android.app.Activity
import android.content.Context
import com.example.demo_full.R



object Animatoo {



    fun animateSlideLeft(context: Context) {
        (context as Activity).overridePendingTransition(
            R.anim.animate_slide_left_enter,
            R.anim.animate_slide_left_exit
        )
    }


}