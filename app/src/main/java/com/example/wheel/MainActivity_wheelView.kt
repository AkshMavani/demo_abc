package com.example.wheel

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMainWheelViewBinding


import com.techdew.lib.HorizontalWheel.AbstractWheel
import com.techdew.lib.HorizontalWheel.ArrayWheelAdapter
import com.techdew.lib.HorizontalWheel.OnWheelScrollListener


class MainActivity_wheelView : AppCompatActivity(), OnWheelScrollListener {
    private lateinit var values: Array<String>
    var arr= listOf("short video","Video","Photo","square","Panorama")
    private lateinit var binding: ActivityMainWheelViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityMainWheelViewBinding.inflate(layoutInflater)
        setContentView(binding.root)




      binding.customHorizontalWheelView.setItems(arr)
        binding.customHorizontalWheelView.setSelectedItemPosition(2)

        binding.customHorizontalWheelView.onItemSelectedListener = { position ->
            Toast.makeText(this, "Selected: ${arr[position]}", Toast.LENGTH_SHORT).show()
        }

//        val scrollChoice: ScrollChoice = findViewById(R.id.scroll_choice) as ScrollChoice
//
//
//        val data: MutableList<String> = ArrayList()
//        data.add("Brazil")
//        data.add("USA")
//        data.add("China")
//        data.add("Pakistan")
//        data.add("Australia")
//        data.add("India")
//        data.add("Nepal")
//        data.add("Sri Lanka")
//        data.add("Spain")
//        data.add("Italy")
//        data.add("France")
//
//        scrollChoice.addItems(data, 4)
//        scrollChoice.setOnItemSelectedListener(object : ScrollChoice.OnItemSelectedListener {
//            override fun onItemSelected(scrollChoice: ScrollChoice?, position: Int, name: String?) {
//                Log.e("ITEM", "onItemSelected:$name ", )
//            }
//        })
    }

    override fun onScrollingStarted(wheel: AbstractWheel?) {
        // You can add your logic here if needed
    }

    override fun onScrollingFinished(wheel: AbstractWheel?) {
        Log.e("item", "onScrollingFinished:${wheel!!.currentItem} ", )
        Log.e("hlo", "onScrollingFinished:${wheel?.currentItem} ", )
    }
}