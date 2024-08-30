package com.example.abc

import android.app.RecoverableSecurityException
import android.content.ContentValues
import android.content.Context
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import java.io.File


object FileUtil {
    private var intentSenderLauncher: ActivityResultLauncher<IntentSender>? = null
    fun initialize(launcher: ActivityResultLauncher<IntentSender>?) {
        intentSenderLauncher = launcher
    }

    fun renameFile(context: Context, filePath: String, newName: String): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val collection =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val projection = arrayOf(MediaStore.Images.Media._ID)
            val selection = MediaStore.Images.Media.DATA + "=?"
            val selectionArgs = arrayOf(filePath)
            val cursor = context.contentResolver.query(
                collection,
                projection,
                selection,
                selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                val fileUri = Uri.withAppendedPath(collection, id.toString())
                val contentValues = ContentValues()
                contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, newName)
                try {
                    val updated = context.contentResolver.update(fileUri, contentValues, null, null)
                    if (updated > 0) {
                        Log.d("FileUtil", "File renamed successfully to $newName")
                        return true
                    }
                } catch (e: RecoverableSecurityException) {
                    if (intentSenderLauncher != null) {
                        val intentSender = e.userAction.actionIntent.intentSender
                        intentSenderLauncher!!.launch(intentSender)
                    } else {
                        Log.e("FileUtil", "IntentSenderLauncher not initialized.")
                    }
                } catch (e: Exception) {
                    Log.e("FileUtil", "File renaming failed: " + e.message)
                } finally {
                    cursor.close()
                }
            } else {
                Log.e("FileUtil", "File not found")
            }
            cursor?.close()
        } else {
            // For Android 9 and below
            val oldFile = File(filePath)
            val extension = oldFile.name.substring(oldFile.name.lastIndexOf("."))
            val newFileName = newName + extension
            val newFile = File(oldFile.parent, newFileName)
            if (oldFile.renameTo(newFile)) {
                Log.d("FileUtil", "File renamed successfully to " + newFile.absolutePath)
                return true
            } else {
                Log.e("FileUtil", "File renaming failed")
            }
        }
        return false
    }
}