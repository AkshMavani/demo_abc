package com.example.demo_full

import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo_full.databinding.ActivityAtivityMoveFouldetBinding
import java.io.File

class Ativity_move_fouldet : AppCompatActivity(), foulder_click {
    lateinit var binding: ActivityAtivityMoveFouldetBinding
    private var fileType: String? = null
    private var filePath: String? = null
    private var fileUri: Uri? = null
    private var destinationFolderPath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAtivityMoveFouldetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        filePath = intent.getStringExtra("file_path")
        fileType = intent.getStringExtra("file_type")
        fileUri = Uri.fromFile(File(filePath!!))

        Log.e("File Path", "filepath:>>>$filePath ")
        Log.e("File Type", "filetype:>>>$fileType ")
        Log.e("File URI", "fileurl:>>>$fileUri ")

        binding.recycleViewMoveFoulder.layoutManager = LinearLayoutManager(this)
        val folderAdapter = FolderAdapter(getMediaFolders(this), this, this)
        binding.recycleViewMoveFoulder.adapter = folderAdapter
    }

    fun getMediaFolders(context: Context): List<Triple<String, String, Long>> {
        val folders = mutableListOf<Triple<String, String, Long>>()
        val seenFolders = mutableSetOf<String>()

        val imageProjection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
        )
        val videoProjection = arrayOf(
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.DATE_TAKEN
        )
        val sortOrder = "${MediaStore.MediaColumns.DATE_TAKEN} DESC"

        context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val folderNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME)
            val imagePathColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val folderName = cursor.getString(folderNameColumn)
                val imagePath = cursor.getString(imagePathColumn)

                if (!seenFolders.contains(folderName)) {
                    seenFolders.add(folderName)
                    folders.add(Triple(folderName, imagePath, System.currentTimeMillis()))
                }
            }
        }

        context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null,
            null,
            sortOrder
        )?.use { cursor ->
            val folderNameColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.BUCKET_DISPLAY_NAME)
            val videoPathColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            while (cursor.moveToNext()) {
                val folderName = cursor.getString(folderNameColumn)
                val videoPath = cursor.getString(videoPathColumn)

                if (!seenFolders.contains(folderName)) {
                    seenFolders.add(folderName)
                    folders.add(Triple(folderName, videoPath, System.currentTimeMillis()))
                }
            }
        }

        return folders
    }

    override fun click(name: String) {
        val baseStoragePath = "/storage/emulated/0"
        destinationFolderPath = "$baseStoragePath/Pictures/$name" // Ensure this is a valid folder

        if (isValidFolder(destinationFolderPath!!)) {
            moveFile(filePath!!, destinationFolderPath!!)
        } else {
            Toast.makeText(this, "Invalid folder. Please select the correct folder type.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidFolder(folderPath: String): Boolean {
        return when (fileType) {
            "IMG" -> folderPath.contains("/Pictures")
            "video" -> folderPath.contains("/Movies") || folderPath.contains("/Videos")
            else -> false
        }
    }

    private fun moveFile(sourceFilePath: String, destinationFolderPath: String) {
        val sourceFile = File(sourceFilePath)
        Log.e("SORA", "moveFile: $sourceFile")
        Log.e("SORA", "moveFile: Destination: $destinationFolderPath")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            moveFileUsingContentResolver(sourceFile, destinationFolderPath)
        } else {
            val destinationFile = File(destinationFolderPath, sourceFile.name)
            if (sourceFile.renameTo(destinationFile)) {
                Toast.makeText(this, "File moved successfully!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "File move failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun moveFileUsingContentResolver(sourceFile: File, destinationFolderPath: String) {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DATA} = ?"
        val selectionArgs = arrayOf(sourceFile.absolutePath)

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                // Prepare ContentValues to update the folder path
                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Movies") // Set valid relative path
                    put(MediaStore.Images.Media.IS_PENDING, 1) // Indicates that the file is being modified
                }

                try {
                    // Update the contentResolver with the new path
                    val rowsUpdated = contentResolver.update(contentUri, values, null, null)
                    if (rowsUpdated > 0) {
                        values.clear()
                        values.put(MediaStore.Images.Media.IS_PENDING, 0) // Modification complete
                        contentResolver.update(contentUri, values, null, null)

                        Toast.makeText(this, "File moved successfully!", Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "File move failed.", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: SecurityException) {
                    // Handle security exceptions by requesting permission
                    requestModifyPermission(sourceFile)
                }
            } else {
                Toast.makeText(this, "File not found in MediaStore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestModifyPermission(sourceFile: File) {
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DATA} = ?"
        val selectionArgs = arrayOf(sourceFile.absolutePath)

        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val fileUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                val uriList = listOf(fileUri)
                val intentSender = MediaStore.createWriteRequest(contentResolver, uriList).intentSender
                try {
                    startIntentSenderForResult(intentSender, 101, null, 0, 0, 0)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Permission request failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "File not found in MediaStore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            filePath?.let { moveFile(it, destinationFolderPath!!) }
        }
    }
}
