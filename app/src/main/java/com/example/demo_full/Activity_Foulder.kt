package com.example.demo_full

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.databinding.ActivityMainBinding


class Activity_Foulder : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var folderAdapter: FolderAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideSystemUI(this)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }

        folderRecyclerView = findViewById(R.id.folderRecyclerView)
        folderRecyclerView.layoutManager = LinearLayoutManager(this)
        folderAdapter = FolderAdapter(getMediaFolders(this), this)
        folderRecyclerView.adapter = folderAdapter
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            folderAdapter = FolderAdapter(getMediaFolders(this), this)
            folderRecyclerView.adapter = folderAdapter
        }
    }

    fun getMediaFolders(context: Context): List<Pair<String, String>> {
        val folders = mutableListOf<Pair<String, String>>()
        val seenFolders = mutableSetOf<String>()

        val imageProjection = arrayOf(MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.Images.Media.DATA)
        val videoProjection = arrayOf(MediaStore.Video.Media.BUCKET_DISPLAY_NAME, MediaStore.Video.Media.DATA)
        val sortOrder = "${MediaStore.MediaColumns.DATE_TAKEN} DESC"

        // Query for images
        val imageCursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            sortOrder
        )

        imageCursor?.use {
            val folderNameColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val imagePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            while (it.moveToNext()) {
                val folderName = it.getString(folderNameColumn)
                val imagePath = it.getString(imagePathColumn)

                if (!seenFolders.contains(folderName)) {
                    seenFolders.add(folderName)
                    folders.add(Pair(folderName, imagePath))
                }
            }
        }

        // Query for videos
        val videoCursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null,
            null,
            sortOrder
        )

        videoCursor?.use {
            val folderNameColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val videoPathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)

            while (it.moveToNext()) {
                val folderName = it.getString(folderNameColumn)
                val videoPath = it.getString(videoPathColumn)

                if (!seenFolders.contains(folderName)) {
                    seenFolders.add(folderName)
                    folders.add(Pair(folderName, videoPath))
                }
            }
        }

        return folders
    }
    fun hideSystemUI(activity: Activity) {
        val decorView = activity.window.decorView
        decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN //| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                //| View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }
}
