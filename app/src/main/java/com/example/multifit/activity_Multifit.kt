package com.example.multifit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo_full.Model_Img
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMultifitBinding

class activity_Multifit : AppCompatActivity() {
    private lateinit var binding:ActivityMultifitBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMultifitBinding.inflate(layoutInflater)
        setContentView(binding.root)
      binding.recycleViewMultifit.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val imgUrlList = intent.getSerializableExtra("img_url") as ArrayList<Model_Img>
        Log.e("IMG090", "onCreate:>>>$imgUrlList ", )
        var adapter=CustomAdapter(imgUrlList)
        binding.recycleViewMultifit.adapter=adapter


    }
}