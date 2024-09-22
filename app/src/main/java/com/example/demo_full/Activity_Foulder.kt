package com.example.demo_full

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.databinding.ActivityMainBinding
import java.io.File


class Activity_Foulder : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var folderRecyclerView: RecyclerView
    private lateinit var folderAdapter: FolderAdapter
    private var fileType: String? = null
    private var filePath: String? = null
    private var fileUri: Uri? = null
    private var destinationFolderPath: String? = null


    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var pendingFilePath: String? = null
    private var pendingNewName: String? = null

    var flag=false
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        hideSystemUI(this)

       // showRadioGroupDialog()
        filePath = intent.getStringExtra("file_path");
        fileType = intent.getStringExtra("file_type");
        Log.e("FileType12", "onCreate:>>>$fileType ")
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)
        }


        folderRecyclerView = findViewById(R.id.folderRecyclerView)
        folderRecyclerView.layoutManager = LinearLayoutManager(this)

        val click = object : foulder_click {
            override fun click(name: String) {
                //val filePath = intent.getStringExtra("filePath")
               // val type = intent.getStringExtra("fileType")
                val flag = intent.getBooleanExtra("flag_foulder", false)

                Log.e("PATH1234", "FilePath: $filePath")
                Log.e("PATH1234", "FolderName: $name")

                if (flag) {
                    fileUri = Uri.fromFile(filePath?.let { File(it) });

                    destinationFolderPath = name;

                    // Check if the selected folder is valid for the file type
                    if (isValidFolder(name)) {
                        moveFile(filePath!!, destinationFolderPath!!);
                    } else {
                        Toast.makeText(this@Activity_Foulder, "Invalid folder. Please select the correct folder type.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    val intent = Intent(this@Activity_Foulder, MainActivity4::class.java)
                    intent.putExtra("Foulder", name)
                    startActivity(intent)
                }
            }
        }

        folderAdapter = FolderAdapter(  getMediaFolders(this), this, click)
        folderRecyclerView.adapter = folderAdapter

    }
    private fun isValidFolder(folderPath: String): Boolean {
        Log.e("FoulderPAth123", "path:$folderPath ", )
        Log.e("FoulderPAth123", "type:$fileType ", )
        if (fileType == "IMG") {
            Log.e("FoulderPAth123", "IMG eqal: ", )
            return true
        } else if (fileType == "video") {
            Log.e("FoulderPAth123", "VIDEO eqal: ", )
            return folderPath.contains("/Movies") || folderPath.contains("/Videos")
        }
        return false
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

//    fun getMediaFolders(context: Context): List<Pair<String, String>> {


    fun getMediaFolders(context: Context): List<Triple<String, String, Long>> {
        val folders = mutableListOf<Triple<String, String, Long>>() // Using Triple to store folder name, path, and date taken
        val seenFolders = mutableSetOf<String>()
        var moviesFolder: Triple<String, String, Long>? = null

        val imageProjection = arrayOf(
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME, // Folder name
            MediaStore.Images.Media.DATA,                // File path
            MediaStore.Images.Media.DATE_TAKEN           // Date taken
        )
        val videoProjection = arrayOf(
            MediaStore.Video.Media.BUCKET_DISPLAY_NAME,  // Folder name
            MediaStore.Video.Media.DATA,                 // File path
            MediaStore.Video.Media.DATE_TAKEN            // Date taken
        )
        val sortOrder = "${MediaStore.MediaColumns.DATE_TAKEN} DESC" // Sort by Date Taken

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
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                var folderName = it.getString(folderNameColumn)
                val imagePath = it.getString(imagePathColumn)
                val dateTaken = it.getLong(dateTakenColumn) // Get the Date Taken value

                // Fallback for missing or null folder names
                if (folderName.isNullOrEmpty()) {
                    folderName = "Unknown"
                }

                if (!seenFolders.contains(folderName)) {
                    seenFolders.add(folderName)
                    if (folderName.equals("Movies", ignoreCase = true)) {
                        moviesFolder = Triple(folderName, imagePath, dateTaken)
                    } else {
                        folders.add(Triple(folderName, imagePath, dateTaken))
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
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                var folderName = it.getString(folderNameColumn)
                val videoPath = it.getString(videoPathColumn)
                val dateTaken = it.getLong(dateTakenColumn) // Get the Date Taken value

                // Fallback for missing or null folder names
                if (folderName.isNullOrEmpty()) {
                    folderName = "Unknown"
                }

                if (!seenFolders.contains(folderName)) {
                    seenFolders.add(folderName)
                    folders.add(Triple(folderName, videoPath, dateTaken))
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
    private fun moveFile(sourceFilePath: String, destinationFolderPath: String) {
        val sourceFile = File(sourceFilePath)
        val destinationFile = File(destinationFolderPath, sourceFile.name)

        // Request modification permission for the file before moving it
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            requestModifyPermission(sourceFile, destinationFile)
        } else {
            // Directly move for Android versions below 11
            if (sourceFile.renameTo(destinationFile)) {
                Toast.makeText(this, "File moved successfully!", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK)
                finish()
            } else {
                Toast.makeText(this, "File move failed.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun requestModifyPermission(sourceFile: File, destinationFile: File) {
        // Query MediaStore to get the content URI for the specific file
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
                // Retrieve the file ID and create the specific content URI
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val fileUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                // Request write permission for this specific file URI
                val uriList = listOf(fileUri)
                val intentSender = MediaStore.createWriteRequest(contentResolver, uriList).intentSender
                try {
                    startIntentSenderForResult(intentSender, 101, null, 0, 0, 0)
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Permission request failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                // File not found in MediaStore
                Toast.makeText(this, "File not found in MediaStore", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun moveFileWithPermission(sourceFile: File, destinationFile: File) {
        Log.e("source123", "moveFileWithPermission:$sourceFile ", )
        //sourceFile  /storage/emulated/0/Pictures/IMG_20240917_194835.jpg
        //destination /storage/emulated/0/Movies/IMG_20240917_194835.jpg
        Log.e("source123", "moveFileWithPermission:$destinationFile ", )
        if (sourceFile.renameTo(destinationFile)) {
            Toast.makeText(this, "File moved successfully!", Toast.LENGTH_SHORT).show()
            setResult(RESULT_OK)
            finish()
        } else {
            Toast.makeText(this, "File move failed.", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, @Nullable data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == RESULT_OK) {
            // If the user grants permission, proceed with moving the file
            moveFileWithPermission(File(filePath), File(destinationFolderPath))
        } else {
            Toast.makeText(this, "Permission denied. Cannot move file.", Toast.LENGTH_SHORT).show()
        }
    }

}
