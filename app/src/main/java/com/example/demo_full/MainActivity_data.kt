package com.example.demo_full

import android.annotation.SuppressLint
import android.app.Activity
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.abc.FileUtil
import com.example.abc.FileUtil.renameFile
import com.example.demo_full.databinding.ActivityMainDataBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class MainActivity_data : AppCompatActivity() {
    private lateinit var binding: ActivityMainDataBinding
    lateinit var adapter: ImagePagerAdapter_DTA
    private lateinit var deleteFileLauncher: ActivityResultLauncher<IntentSenderRequest>



    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDataBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intentSenderLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "File renamed successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to get user permission", Toast.LENGTH_SHORT).show()
            }
        }
        val data = intent.getStringExtra("img")
        val position = intent.getIntExtra("position", 0)
        val imageList = intent.getStringArrayListExtra("imgList") as ArrayList<Model_Img>
        val clickImg = object : click_img {
            override fun click(img: Model_Img) {
                Log.e("PDF123", "click:>>>PDF#####${img.str}")
                binding.ll.visibility = View.VISIBLE
                binding.buttonDelete.setOnClickListener {
                    if (img.type == "IMG") {
                        val imageUri = getImageContentUri(img.str)
                        if (imageUri != null) {
                            deleteFileWithConfirmation(imageUri, false)
                        } else {
                            Log.e("TAG", "Could not get content URI for the image")
                        }
                    } else {
                        val imageUri = getVideoContentUri(img.str)
                        if (imageUri != null) {
                            deleteFileWithConfirmation(imageUri, true)
                        } else {
                            Log.e("TAG", "Could not get content URI for the video")
                        }
                    }
                }
                binding.buttonPdf.setOnClickListener {
//                    Log.e("PDF", "img:>>>$img")
//                    val pdfUri = convertImageToPdf(img.str)
//                    if (pdfUri != null) {
//                        Log.e("PDF", "uri:$pdfUri")
//                        openFileManagerForSaving(pdfUri)
//                    } else {
//                        Toast.makeText(this@MainActivity_data, "Failed to convert image to PDF", Toast.LENGTH_SHORT).show()
//                    }
                  //  FileUtil.initialize(intentSenderLauncher);

                    val filePath = "/storage/emulated/0/Pictures/IMG_20240826_185729.jpg"
                    val newName = "new_image_name"

                    if (!renameFile(this@MainActivity_data, img.str, newName)) {
                        Toast.makeText(this@MainActivity_data, "File renaming failed.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@MainActivity_data, "File renamed successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        deleteFileLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                adapter.notifyDataSetChanged()
                Toast.makeText(this, "File deleted successfully", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "File deletion canceled", Toast.LENGTH_SHORT).show()
            }
        }

        adapter = ImagePagerAdapter_DTA(this, imageList, clickImg)
        Log.e("TAG123", "onCreate: >>POs$position")
        Log.e("TAG123", "onCreate: >>POs$imageList")

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setCurrentItem(position, false)
    }

    private fun deleteFileWithConfirmation(fileUri: Uri, isVideo: Boolean) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                Log.e("TAG", "deleteFileWithConfirmation: URI: $fileUri")
                val deleteIntent = MediaStore.createDeleteRequest(contentResolver, listOf(fileUri))
                deleteFileLauncher.launch(IntentSenderRequest.Builder(deleteIntent.intentSender).build())
            } else {
                Log.e("TAG", "deleteFileWithConfirmation: Unsupported version")
            }
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun getImageContentUri(filePath: String): Uri? {
        val file = File(filePath)
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Images.Media.DATA + "=?",
            arrayOf(file.absolutePath),
            null
        )
        return cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            } else {
                null
            }
        }
    }

    private fun getVideoContentUri(filePath: String): Uri? {
        val file = File(filePath)
        val projection = arrayOf(MediaStore.Video.Media._ID)
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            MediaStore.Video.Media.DATA + "=?",
            arrayOf(file.absolutePath),
            null
        )
        return cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
            } else {
                null
            }
        }
    }

    private fun convertImageToPdf(imagePath: String): Uri? {
        val bitmap: Bitmap? = BitmapFactory.decodeFile(imagePath)


        if (bitmap == null) {
            Toast.makeText(this, "Failed to decode image", Toast.LENGTH_SHORT).show()
            return null
        }

        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(bitmap.width, bitmap.height, 1).create()
        val page = document.startPage(pageInfo)

        page.canvas.drawBitmap(bitmap, 0f, 0f, null)
        document.finishPage(page)

        val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "imgtopdf.pdf")
        return try {
            FileOutputStream(file).use { outputStream ->
                document.writeTo(outputStream)
            }
            document.close()
            bitmap.recycle()
            Uri.fromFile(file)
        } catch (e: IOException) {
            e.printStackTrace()
            document.close()
            bitmap.recycle()
            null
        }
    }

    private fun openFileManagerForSaving(pdfUri: Uri) {
        val intent = Intent(Intent.ACTION_CREATE_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/pdf"
            putExtra(Intent.EXTRA_TITLE, "imgtopdf.pdf")
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pdfUri)
        }

        startActivityForResult(intent, 1001)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1001 && resultCode == RESULT_OK) {
            data?.data?.also { uri ->
                Toast.makeText(this, "PDF saved successfully!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun renameFile(context: Context, filePath: String, newName: String): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            renameFileScopedStorage(context, filePath, newName)
        } else {
            renameFileLegacy(filePath, newName)
        }
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun renameFileScopedStorage(context: Context, filePath: String, newName: String): Boolean {
        val collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val selection = "${MediaStore.Images.Media.DATA}=?"
        val selectionArgs = arrayOf(filePath)

        val cursor: Cursor? = context.contentResolver.query(collection, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
            val fileUri = Uri.withAppendedPath(collection, id.toString())

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, newName)
            }

            try {
                val updated = context.contentResolver.update(fileUri, contentValues, null, null)
                if (updated > 0) {
                    Log.d("FileUtil", "File renamed successfully to $newName")
                    return true
                }
            } catch (e: RecoverableSecurityException) {
                val intentSender: IntentSender = e.userAction.actionIntent.intentSender
                intentSenderLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            } catch (e: Exception) {
                Log.e("FileUtil", "File renaming failed: ${e.message}")
            } finally {
                cursor.close()
            }
        } else {
            Log.e("FileUtil", "File not found")
        }
        cursor?.close()
        return false
    }

    private fun renameFileLegacy(filePath: String, newName: String): Boolean {
        val oldFile = File(filePath)
        val extension = oldFile.name.substring(oldFile.name.lastIndexOf("."))
        val newFileName = "$newName$extension"
        val newFile = File(oldFile.parent, newFileName)

        return if (oldFile.renameTo(newFile)) {
            Log.d("FileUtil", "File renamed successfully to ${newFile.absolutePath}")
            true
        } else {
            Log.e("FileUtil", "File renaming failed")
            false
        }
    }




}
