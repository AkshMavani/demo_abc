package com.example.blur

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityViewPagerBinding

class ViewPagerActivity : AppCompatActivity() {
    private lateinit var binding:ActivityViewPagerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageList = intent.getStringArrayListExtra("image_list")
        val currentPosition = intent.getIntExtra("image_position", 0)

        if (imageList != null) {

            val adapter = ImagePagerAdapter(imageList)
          binding.viewPager.adapter = adapter

            // Set the initial position to the clicked image
           binding.viewPager.setCurrentItem(currentPosition, false)
        }
    }
}