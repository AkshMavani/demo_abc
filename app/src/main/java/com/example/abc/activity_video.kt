package com.example.abc

import android.content.Context
import android.content.pm.ActivityInfo
import android.media.AudioManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.MediaController
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityVideoBinding

class activity_video : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    private var mediaController: MediaController? = null

    private var progress_dta: Int = 0
    private var isMuted = false
    private var isLandscape = false
    private var isPlaying = false
    private var lastVolume = 0
    private lateinit var audioManager: AudioManager
    private var handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("VIDEO_URL")
        audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mediaController = MediaController(this@activity_video)
        binding.videoView.setMediaController(null)
        binding.videoView.setVideoPath(videoUrl)

        binding.videoView.setOnPreparedListener {
            binding.txtEndTime.text = formatDuration(binding.videoView.duration)
            binding.seekBar.max = binding.videoView.duration
            binding.videoView.start()
            updateSeekBar()
            binding.playPauseButton.isEnabled = true
        }

        binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    binding.videoView.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        binding.playPauseButton.isEnabled = false
        binding.playPauseButton.setOnClickListener {
            if (isPlaying) {
                pauseVideo()
            } else {
                playVideo()
            }
        }

        binding.volumeButton.setOnClickListener {
            if (isMuted) {
                turnOnMusic()
            } else {
                turnOffMusic()
            }
        }

        binding.splitBtn.setOnClickListener {
            isLandscape = !isLandscape
            requestedOrientation = if (isLandscape) {
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            } else {
                ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
            }
        }
    }

    private fun playVideo() {
        binding.videoView.start()
        isPlaying = true
        binding.playPauseButton.setImageResource(R.drawable.baseline_pause_24)
        updateSeekBar()
    }

    private fun pauseVideo() {
        binding.videoView.pause()
        isPlaying = false
        binding.playPauseButton.setImageResource(R.drawable.baseline_play_arrow_24)
        handler.removeCallbacks(runnable)
    }

    private fun updateSeekBar() {
        runnable = object : Runnable {
            override fun run() {
                binding.txtTime.text = formatDuration(binding.videoView.currentPosition)
                binding.seekBar.progress = binding.videoView.currentPosition
                handler.postDelayed(this, 1000) // Update every second
            }
        }
        handler.post(runnable)
    }

    override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    private fun formatDuration(duration: Int): String {
        val hours = duration / 3600000
        val minutes = (duration % 3600000) / 60000
        val seconds = (duration % 60000) / 1000
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun turnOnMusic() {
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, lastVolume, 0)
        isMuted = false
    }

    private fun turnOffMusic() {
        lastVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0)
        isMuted = true
    }
}
