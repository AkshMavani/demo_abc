package com.example.gallery.ui

import android.content.Context
import androidx.lifecycle.LiveData

import androidx.lifecycle.MutableLiveData

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gallery.ui.model.GalleryModel


class MediaViewModel(private val repository: MediaRepository) : ViewModel() {
    private val _mediaItemsLiveData: MutableLiveData<List<MediaItem>> = MutableLiveData()
    private val _galleryItemsLiveData: MutableLiveData<List<GalleryModel>> = MutableLiveData()
    private var _yearLiveData:MutableLiveData<List<GalleryModel>> = MutableLiveData()

    // Public getters for LiveData
    val mediaItemsLiveData: LiveData<List<MediaItem>> get() = _mediaItemsLiveData
    val galleryItemsLiveData: LiveData<List<GalleryModel>> get() = _galleryItemsLiveData


    init {
        loadMediaItems()
        loadGalleryItems()
    }

    // Function to load MediaItems
    private fun loadMediaItems() {
        val mediaItems: List<MediaItem> = repository.fetchAllMedia()
        _mediaItemsLiveData.value = mediaItems
    }

    // Function to load GalleryItems
    private fun loadGalleryItems() {
        val galleryItems: List<GalleryModel> = repository.setImages()
        _galleryItemsLiveData.value = galleryItems
    }

}


class MediaViewModelFactory(private val repository: MediaRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MediaViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MediaViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
