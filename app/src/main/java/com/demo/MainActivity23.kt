package com.demo

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R


class MainActivity23 : AppCompatActivity() {
    var id1 = "test_channel_01"
    var rpl: ActivityResultLauncher<Array<String>>? = null
    private val REQUIRED_PERMISSIONS = arrayOf<String>(Manifest.permission.POST_NOTIFICATIONS)
    var TAG = "MainActivity"

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main23)
        val btn=findViewById<Button>(R.id.btn_Start)
        val btnstop=findViewById<Button>(R.id.btn_stop)
        btn.setOnClickListener {
            val serviceIntent = Intent(this, MyForegroundService::class.java)
            startService(serviceIntent)
        }
        btnstop.setOnClickListener {
            stopService(Intent(this, MyForegroundService::class.java))
        }

    }
}