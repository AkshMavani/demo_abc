package com.foregorund.adapter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import com.example.demo_full.R

class MainActivity22 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main22)
        val myIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION)
        startActivity(myIntent)
                val intent=Intent(this,MyForegroundService::class.java)
        startService(intent)
    }
}