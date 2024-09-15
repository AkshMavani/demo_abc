package com.example.abc



import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.blurview.HorizontalWheelView

import com.example.demo_full.R
import java.util.Arrays


class MainActivity15 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main15)

        val wheelViewPicker = findViewById<HorizontalWheelView>(R.id.wheel_view_picker)
        val items = Arrays.asList("Item 1", "Item 2", "Item 3", "Item 4")
        wheelViewPicker.setItems(items)

        wheelViewPicker.setOnItemSelectedListener { position -> // Handle item selection
            Log.d("WheelViewPicker", "Selected item: " + items[position])
        }
    }
}