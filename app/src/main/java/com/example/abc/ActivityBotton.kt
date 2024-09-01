package com.example.abc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.demo_full.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class ActivityBotton : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_botton)
        val frame=findViewById<FrameLayout>(R.id.frame)
        val img=findViewById<ImageView>(R.id.img)
        img.setOnClickListener {
            Toast.makeText(this,"click",Toast.LENGTH_SHORT).show()
        }
//        val bottomSheet = BottomSheetDialog()
//        bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
        BottomSheetBehavior.from(frame).apply {
            peekHeight=200
            state=BottomSheetBehavior.STATE_COLLAPSED
        }

    }
}