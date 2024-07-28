package com.example.demo_full

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.example.demo_full.databinding.ActivityMain5Binding

class MainActivity5 : AppCompatActivity() {
    private lateinit var binding: ActivityMain5Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain5Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val data = intent.getStringExtra("img_url")
        Log.e("TAG_DTA", "onCreate:>>>>$data ",)
        val flag = intent.getBooleanExtra("flag", false)
        if (flag) {
            binding.videoViewFull.visibility = View.VISIBLE
            binding.imageViewFull.visibility = View.GONE
            binding.videoViewFull.setVideoURI(data?.toUri())
            binding.videoViewFull.setOnPreparedListener { mediaPlayer ->
                mediaPlayer.isLooping = true
                binding.videoViewFull.start()
            }

        } else {
            binding.videoViewFull.visibility = View.GONE
            binding.imageViewFull.visibility = View.VISIBLE

            Glide.with(this)
                .load(data)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.imageViewFull)
        }

    }
    fun music() {
        val mp: MediaPlayer = MediaPlayer.create(this, R.raw.sound)
        mp.start()
        Handler().postDelayed(Runnable {
            mp.stop()
            mp.release()
        }, 1000)
        mp.setOnCompletionListener { mp ->
            mp.release()
        }
    }
}