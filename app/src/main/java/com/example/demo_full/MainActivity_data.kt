package com.example.demo_full

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.demo_full.databinding.ActivityMainDataBinding

class MainActivity_data : AppCompatActivity() {
    private lateinit var binding:ActivityMainDataBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val data=intent.getStringExtra("img")
        Log.e("data23", "onCreate:$data ", )
        Glide.with(this)
            .load(data)
            .centerCrop()
            .placeholder(R.drawable.ic_launcher_background)
            .into(binding.imgUrl)
    }
}