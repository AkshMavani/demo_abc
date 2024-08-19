package com.example.abc

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.demo_full.R
import com.otaliastudios.cameraview.BitmapCallback
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.frame.Frame
import com.otaliastudios.cameraview.frame.FrameProcessor

class Activity_Camera1 : AppCompatActivity() {
    private lateinit var cameraView: CameraView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera1)
        cameraView = findViewById(R.id.cameraView12)
        cameraView.setLifecycleOwner(this)
        cameraView.addFrameProcessor(object : FrameProcessor {
            override fun process(frame: Frame) {
                Log.e("Process12", "formate:>>${frame.format} ")
                Log.e("Process12", "frame:>>${frame} ",)
                Log.e("Process12", "rotation:>>${frame.rotation} ")
                Log.e("Process12", "rotation user:>>${frame.rotationToUser} ")
                Log.e("Process12", "rotationview:>>${frame.rotationToView} ")
                Log.e("Process12", "size:>>${frame.size} ")
                Log.e("Process12", "frezz:>>${frame.freeze()} ")
                Log.e("Process12", "relse:>>${frame.release()} ")
                Log.e("Process12", "relse:>>${frame} ")
            }
        })

    }

}