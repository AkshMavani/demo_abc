package com.example.blur

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.recyclerview.widget.GridLayoutManager
import com.example.abc.ImageAdapter
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityMain14Binding

class MainActivity14 : AppCompatActivity() {
    private lateinit var binding:ActivityMain14Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMain14Binding.inflate(layoutInflater)
        setContentView(binding.root)

        val folderPath = intent.getStringExtra("folder_path") ?: return

        binding.rc14.layoutManager = GridLayoutManager(this, 3)
       val imageAdapter = ImageAdapter1(emptyList())
        binding.rc14.adapter = imageAdapter

        // Fetch images from the folder
        val imageList = getImagesFromFolder(this, folderPath)
        imageAdapter.updateImages(imageList)
    }
    fun getImagesFromFolder(context: Context, folderPath: String): List<String> {
        val imageList: MutableList<String> = mutableListOf()
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        val selection = "${MediaStore.Images.Media.DATA} LIKE ?"
        val selectionArgs = arrayOf("$folderPath%")

        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            MediaStore.Images.Media.DATE_MODIFIED + " DESC"
        )

        cursor?.use {
            val dataColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val imagePath = it.getString(dataColumn)
                imageList.add(imagePath)
            }
        }
        return imageList
    }
}