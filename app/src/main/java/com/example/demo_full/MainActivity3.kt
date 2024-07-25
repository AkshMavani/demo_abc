package com.example.demo_full

import android.content.Context
import android.media.AudioManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import com.example.demo_full.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
   private lateinit var binding: ActivityMain3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val sharepref=getSharedPreferences("PREF", MODE_PRIVATE)
        val edit=sharepref.edit()
//        if (binding.switch1.seto){
//            Log.e("TAGzzz", "onCreate:>>ON ", )
//            edit.putBoolean("VOLUMEUP",true).apply()
//        }else{
//            edit.putBoolean("VOLUMEUP",false).apply()
//        }
       binding.switch1.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // Switch is ON
                edit.putBoolean("VOLUMEUP",true).apply()
                Log.e("TAG1234c5", "Switch is ON")
            } else {
                // Switch is OFF
                edit.putBoolean("VOLUMEUP",false).apply()
                Log.e("TAG1234c5", "Switch is OFF")
            }
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        if (event.action == KeyEvent.ACTION_DOWN) {
            when (event.keyCode) {
                KeyEvent.KEYCODE_VOLUME_UP -> {
                    return true
                }
                KeyEvent.KEYCODE_VOLUME_DOWN -> {
                    return true
                }
            }
        }
        return super.dispatchKeyEvent(event)
    }
}
