package com.example.demo_full


import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.databinding.ActivityMainBinding
import com.panoramagl.utils.PLUtils


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val bitmaps = arrayOfNulls<Bitmap>(2)

    private val buttonClickListener =
        View.OnClickListener { v ->
            when (v.id) {
                R.id.button1 -> changePanorama(0)
                R.id.button2 -> changePanorama(1)
                else -> {}
            }
        }

    private val useAcceleratedTouchScrolling = false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        bitmaps[0] = PLUtils.getBitmap(this, R.raw.sighisoara_sphere);
        bitmaps[1] = PLUtils.getBitmap(this, R.raw.sighisoara_sphere);


       binding.sphericalView.setPanorama(bitmaps[0], true);







    }
    override fun onResume() {
        super.onResume()
       binding.sphericalView.onResume()
    }

    override fun onPause() {
        super.onPause()
       binding.sphericalView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
       binding.sphericalView.onDestroy()
    }

    private fun changePanorama(index: Int) {
       binding.sphericalView.setPanorama(bitmaps[index], true)
    }

}