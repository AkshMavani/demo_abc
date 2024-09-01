package com.example.demo_full

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.databinding.ActivityMainBinding
import java.io.File

class Activity_Foulder : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var folderAdapter: FolderAdapter
    var flag=false
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

        val click = object : foulder_click {
            override fun click(name: String) {
                val filePath = intent.getStringExtra("filePath")
                val type = intent.getStringExtra("fileType")
                val flag = intent.getBooleanExtra("FLAG", false)

                Log.e("PATH1234", "FilePath: $filePath")
                Log.e("PATH1234", "Type: $type")
                Log.e("PATH1234", "FolderName: $name")

                if (flag) {
                    if (filePath != null && name.isNotEmpty()) {

                        moveMediaToFolder(filePath, name)
                    } else {
                        Toast.makeText(this@Activity_Foulder, "File path or folder name is missing", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    val intent = Intent(this@Activity_Foulder, MainActivity4::class.java)
                    intent.putExtra("Foulder", name)
                    startActivity(intent)
                }
            }
        }

        folderAdapter = FolderAdapter(getMediaFolders(this), this, click)
        folderRecyclerView.adapter = folderAdapter

    }

    private fun moveMediaToFolder(mediaPath: String, folderName: String) {
        val srcFile = File(mediaPath)
        val destDir = File(getExternalFilesDir(null), folderName)

        // Ensure the destination directory exists
        if (!destDir.exists()) {
            if (!destDir.mkdirs()) {
                Toast.makeText(this, "Failed to create destination directory: ${destDir.absolutePath}", Toast.LENGTH_SHORT).show()
                return
            }
        }

        val destFile = File(destDir, srcFile.name)

        try {
            // Copy the file to the new location
            srcFile.copyTo(destFile, overwrite = true)

            // Check if the copy was successful
            if (destFile.exists()) {
                // Delete the original file
                val srcDocumentFile = DocumentFile.fromFile(srcFile)
                if (srcDocumentFile.delete()) {
                    // Notify the MediaScanner to remove the original file
                    MediaScannerConnection.scanFile(
                        this,
                        arrayOf(srcFile.absolutePath),
                        null
                    ) { _, _ ->
                        runOnUiThread {
                            Toast.makeText(this, "File moved successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }
                } else {
                    throw Exception("Failed to delete the original file after copying")
                }
            } else {
                throw Exception("Failed to move the file. Destination file does not exist.")
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Error moving file: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val click = object : foulder_click {
                override fun click(name: String) {
                    val intent = Intent(this@Activity_Foulder, MainActivity4::class.java)
                    intent.putExtra("Foulder", name)
                    startActivity(intent)
                }
            }
            folderAdapter = FolderAdapter(getMediaFolders(this), this, click)
            folderRecyclerView.adapter = folderAdapter
        }
    }

    fun getMediaFolders(context: Context): List<Pair<String, String>> {
        val folders = mutableListOf<Pair<String, String>>()
        val seenFolders = mutableSetOf<String>()
        var moviesFolder: Pair<String, String>? = null

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
                    if (folderName.equals("Movies", ignoreCase = true)) {
                        moviesFolder = Pair(folderName, imagePath)
                    } else {
                        folders.add(Pair(folderName, imagePath))
                    }
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
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                or View.SYSTEM_UI_FLAG_IMMERSIVE)
    }
}
