package com.example.demo_full

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.demo_full.databinding.ActivityMain4Binding


class MainActivity4 : AppCompatActivity() {

    private lateinit var binding: ActivityMain4Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain4Binding.inflate(layoutInflater)
        setContentView(binding.root)
        val intent = intent.getStringExtra("Foulder")
        Log.e("FLP", "onCreate:$intent ",)
        val a = getMediaInFolder(this, intent.toString())
        Log.e("FLP", "onCreate:>>>>>>>>>>>>>>>$a ",)
        val imageRecyclerView = findViewById<RecyclerView>(R.id.imageRecyclerView)

        imageRecyclerView.layoutManager = GridLayoutManager(this, 3)


        val images = intent?.let { getMediaInFolder(this, it) }
        val imageAdapter = images?.let { ImageAdapter(it, this) }
        imageRecyclerView.adapter = imageAdapter
    }

    fun getMediaInFolder(context: Context, folderName: String): List<String> {
        val mediaList = mutableListOf<String>()

        // Query images
        val imageProjection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf(folderName)
        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val imageCursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            selection,
            selectionArgs,
            sortOrder
        )
        imageCursor?.use {
            val imagePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (it.moveToNext()) {
                val imagePath = it.getString(imagePathColumn)
                mediaList.add(imagePath)
            }
        }
        val videoProjection = arrayOf(MediaStore.Video.Media.DATA)
        val videoCursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            selection,
            selectionArgs,
            sortOrder
        )

        videoCursor?.use {
            val videoPathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (it.moveToNext()) {
                val videoPath = it.getString(videoPathColumn)
                mediaList.add(videoPath)
            }
        }

        return mediaList
    }


}
