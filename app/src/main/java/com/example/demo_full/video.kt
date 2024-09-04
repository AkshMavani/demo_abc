package com.example.demo_full

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import life.knowledge4.videotrimmer.K4LVideoTrimmer
import life.knowledge4.videotrimmer.interfaces.OnTrimVideoListener


class video : AppCompatActivity(),OnTrimVideoListener {
    private lateinit var videoTrimmer: K4LVideoTrimmer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video2)
        val inteent=intent.getStringExtra("video")
        Log.e("videourl", "onCreate:>>>$inteent ")

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)

        val videoUriString = intent.getStringExtra("video")
        Log.e("videourl", "onCreate:>>>$videoUriString ")

        videoTrimmer = findViewById<View>(R.id.timeLine) as K4LVideoTrimmer
        videoTrimmer.setMaxDuration(30) // Set maximum duration in seconds
        videoTrimmer.setOnTrimVideoListener(this)

        videoUriString?.let {
            videoTrimmer.setVideoURI(Uri.parse(it))
        }
    }

    override fun getResult(uri: Uri?) {
        uri?.let {
            Log.d("TrimmedVideoPath", "Trimmed video saved at: ${it.path}")
            // You can handle the trimmed video URI here
        }
    }

    override fun cancelAction() {

    }

//    override fun cancelAction() {
//        videoTrimmer.destroy()
//        finish()
//    }
//
//    override fun onTrimStarted() {
//
//    }
//
//    override fun onError(message: String?) {
//
//    }
//
//    override fun onVideoPrepared() {
//
//    }
}