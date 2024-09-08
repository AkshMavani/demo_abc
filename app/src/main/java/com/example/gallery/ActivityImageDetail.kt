package com.example.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityImageDetailBinding
import com.example.gallery.ui.MediaAdapter
import com.example.gallery.ui.MediaRepository
import com.example.gallery.ui.MediaViewModel
import com.example.gallery.ui.MediaViewModelFactory
import com.example.gallery.ui.adapter.MediaPagerAdapter

class ActivityImageDetail : AppCompatActivity() {
    private lateinit var mediaViewModel: MediaViewModel
    lateinit var mediaAdapter:MediaPagerAdapter
    private lateinit var binding:ActivityImageDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityImageDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val repository = MediaRepository(this)

        // Create the MediaViewModel using the factory
        mediaViewModel = ViewModelProvider(this, MediaViewModelFactory(repository))[MediaViewModel::class.java]

        // Observe the galleryItemsLiveData
        mediaViewModel.galleryItemsLiveData.observe(this, { galleryItems ->
            Log.e("MODEL_999", "onCreate:>>$galleryItems ", )
            mediaAdapter = MediaPagerAdapter(galleryItems)

            binding.pagerPhotos.adapter=mediaAdapter
        })
//        val selectedPosition = intent.getIntExtra("selectedPosition", 0)

//        viewPager.adapter = mediaAdapter
//
//        viewPager.setCurrentItem(selectedPosition, false)
        val url=intent.getStringExtra("image")
        val position=intent.getIntExtra("position",0)
//        mediaViewModel.galleryItemsLiveData.observe(this, Observer { mediaItems ->
//            Log.e("Gallery", "onCreateView:>>>>>$mediaItems ")
//              mediaAdapter = MediaPagerAdapter(mediaItems)
//
//
//        })
        binding.pagerPhotos.currentItem=position



    }
}