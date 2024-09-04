package com.example.abc

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMainActivityqrcodeBinding

class MainActivityqrcode : AppCompatActivity() {
    private lateinit var binding:ActivityMainActivityqrcodeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainActivityqrcodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}