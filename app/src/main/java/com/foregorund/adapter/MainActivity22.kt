package com.foregorund.adapter

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMain22Binding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lib.customedittext.SlideLayout

class MainActivity22 : AppCompatActivity() {
    private lateinit var binding:ActivityMain22Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain22Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val textMessage: TextView = findViewById(R.id.message)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> textMessage.setText(R.string.title_home)
                R.id.navigation_dashboard -> textMessage.setText(R.string.title_dashboard)
                R.id.navigation_general_iamge -> textMessage.setText(R.string.title_notifications)
                R.id.navigation_setting -> textMessage.setText(R.string.text_setting)

                else -> return@setOnNavigationItemSelectedListener false
            }
            true
        }
      /*  binding.sliderlayout.setOnStateChangeListener(object : SlideLayout.OnStateChangeListener() {
            override fun onStateChanged(layout: SlideLayout?, open: Boolean) {
                Log.e("get_Event", "onStateChanged: ", )
            }


        })*/


        /*   val myIntent = Intent(Settings.ACTION_MAN
           startActivity(myIntent)
                   val intent=Intent(this,MyForegroundService::class.java)
           startService(intent)*/

    }
}