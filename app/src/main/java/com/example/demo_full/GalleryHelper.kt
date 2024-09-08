package com.example.demo_full

import android.content.Context
import android.database.Cursor
import android.os.Environment
import android.provider.MediaStore

object GalleryHelper {
    fun getLastItemFromCameraFolder(context: Context): String? {
        val cameraFolderPath =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath + "/Camera/"

        // Get the latest image
        val latestImage = getLatestMediaFile(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cameraFolderPath)

        // Get the latest video
        val latestVideo = getLatestMediaFile(context, MediaStore.Video.Media.EXTERNAL_CONTENT_URI, cameraFolderPath)

        // Compare the dates of the latest image and video, return the most recent one
        return when {
            latestImage != null && latestVideo != null -> {
                if (latestImage.second > latestVideo.second) latestImage.first else latestVideo.first
            }
            latestImage != null -> latestImage.first
            latestVideo != null -> latestVideo.first
            else -> null
        }
    }

    // Helper function to query MediaStore and get the latest media file
    private fun getLatestMediaFile(context: Context, uri: android.net.Uri, cameraFolderPath: String): Pair<String, Long>? {
        val projection = arrayOf(
            MediaStore.MediaColumns.DATA,   // File path
            MediaStore.MediaColumns.DATE_ADDED // Date added
        )

        val selection = MediaStore.MediaColumns.DATA + " like ? "
        val selectionArgs = arrayOf("$cameraFolderPath%")
        val sortOrder = MediaStore.MediaColumns.DATE_ADDED + " DESC"

        val cursor: Cursor? = context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)

        cursor?.use {
            if (it.moveToFirst()) {
                val filePath = it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA))
                val dateAdded = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED))
                return Pair(filePath, dateAdded)
            }
        }
        return null
    }
}
