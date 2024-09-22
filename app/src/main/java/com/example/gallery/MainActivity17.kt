package com.example.gallery

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.databinding.ActivityMain17Binding
import com.example.gallery.util.DragLayout

class MainActivity17 : AppCompatActivity(){
    private var firstStart = true
    private lateinit var binding:ActivityMain17Binding
    private var firstFinish = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain17Binding.inflate(layoutInflater)
        setContentView(binding.root)




    }



}