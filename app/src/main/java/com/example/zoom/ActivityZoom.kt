package com.example.zoom

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityZoomBinding

class ActivityZoom : AppCompatActivity() {
    private lateinit var binding:ActivityZoomBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityZoomBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //binding.imageView.setImage(R.drawable.sticker_ic_flip_white_18dp    )

    }
}