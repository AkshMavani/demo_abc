package com.example.abc

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.MediaController
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.example.demo_full.databinding.ActivityVideoBinding

class activity_video : AppCompatActivity() {
    private lateinit var binding: ActivityVideoBinding
    private var mediaController: MediaController? = null
    private val handler = Handler()
    var progress_dta:Int=0
    private var isMuted = false
    private var isLandscape = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videoUrl = intent.getStringExtra("VIDEO_URL")
        mediaController = MediaController(this@activity_video)
        binding.videoView.setMediaController(null)

        binding.videoView.setVideoURI(videoUrl?.toUri())
        binding.videoView.start()

        binding.playPauseButton.setOnClickListener {
            if (binding.videoView.isPlaying) {
                binding.videoView.pause()
                binding.playPauseButton.setImageResource(android.R.drawable.ic_media_play)
            } else {
                binding.videoView.start()
                binding.playPauseButton.setImageResource(android.R.drawable.ic_media_pause)
            }
        }

        // Set up the seek bar
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            Log.e("TAG_DTA", "onCreate:>>>${binding.videoView.duration} ")
            handler.postDelayed(object : Runnable {
                override fun run() {
                    binding.seekBar.progress = binding.videoView.currentPosition
                    val data=formatDuration(binding.videoView.duration.toLong())
                    binding.txtTime.text=data.toString()
                    handler.postDelayed(this, 100)
                }
            }, 100)

            binding.seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    progress_dta=progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {}
                override fun onStopTrackingTouch(seekBar: SeekBar) {

                        binding.videoView.seekTo(progress_dta)

                }
            })
            binding.volumeButton.setOnClickListener {
                if (isMuted) {
                    mediaPlayer.setVolume(1f, 1f)
                    binding.volumeButton.setImageResource(android.R.drawable.ic_lock_silent_mode_off)
                    isMuted = false
                } else {
                    mediaPlayer.setVolume(0f, 0f)
                    binding.volumeButton.setImageResource(android.R.drawable.ic_lock_silent_mode)
                    isMuted = true
                }
            }

        }
        binding.splitBtn.setOnClickListener {
            if (isLandscape) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                isLandscape = false;
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                isLandscape = true;
            }
        }


        binding.videoView.setOnCompletionListener {
            binding.playPauseButton.setImageResource(android.R.drawable.ic_media_play)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null)
    }



    fun formatDuration(duration: Long): String? {
        val hours = duration / 3600000
        val minutes = duration % 3600000 / 60000
        val seconds = duration % 60000 / 1000
        var timeString = ""
        if (hours > 0) {
            timeString += hours.toString() + "h "
        }
        if (minutes > 0) {
            timeString += minutes.toString() + "m "
        }
        timeString += seconds.toString() + "s"
        return timeString.trim { it <= ' ' }
    }

}
