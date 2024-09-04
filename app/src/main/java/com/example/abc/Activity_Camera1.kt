package com.example.abc

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityCamera1Binding
import com.example.filte.FilterAdapter
import com.example.filte.Model_Filter
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.filter.Filters


class Activity_Camera1 : AppCompatActivity() {
    private lateinit var cameraView: CameraView
    var arr:ArrayList<Model_Filter> = ArrayList()
    private lateinit var binding:ActivityCamera1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCamera1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        cameraView = findViewById(R.id.cameraView12)
        cameraView.setLifecycleOwner(this)
        Log.e("camera12", "onCreate:>>>${cameraView.filter} ")
        Log.e("camera12", "onCreate:>>>${cameraView.filter} ")
         val allFilters = Filters.values()
        for (i in allFilters){
            Log.e("TAG123", "onCreate:>>>>${i.name} ")
            arr.add(Model_Filter(i))

        }
       val HorizontalLayout = LinearLayoutManager(
            this@Activity_Camera1,
            LinearLayoutManager.HORIZONTAL,
            false
        )
       binding.recycleviewFilter.setLayoutManager(HorizontalLayout)
        val adapter=FilterAdapter(arr)
        binding.recycleviewFilter.adapter=adapter
//        cameraView.addFrameProcessor(object : FrameProcessor {
//            override fun process(frame: Frame) {
//                Log.e("Process12", "formate:>>${frame.format} ")
//                Log.e("Process12", "frame:>>${frame} ")
//                Log.e("Process12", "rotation:>>${frame.rotation} ")
//                Log.e("Process12", "rotation user:>>${frame.rotationToUser} ")
//                Log.e("Process12", "rotationview:>>${frame.rotationToView} ")
//                Log.e("Process12", "size:>>${frame.size} ")
//                Log.e("Process12", "frezz:>>${frame.freeze()} ")
//                Log.e("Process12", "relse:>>${frame.release()} ")
//                Log.e("Process12", "relse:>>${frame} ")
//            }
//        })

    }

}