package com.example.demo_full

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.RecoverableSecurityException
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.print.PrintManager
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.demo_full.databinding.ActivityMainDataBinding
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException


class MainActivity_data : AppCompatActivity() {
    private lateinit var binding: ActivityMainDataBinding
    lateinit var adapter: ImagePagerAdapter_DTA
    private lateinit var deleteFileLauncher: ActivityResultLauncher<IntentSenderRequest>

    private val PDF_FILE_NAME = "sample.pdf"

    private var pendingFilePath: String? = null
    private var pendingNewName: String? = null

    private var pendingFilePath1: String? = null
    var click=false
    private var pendingIntent: Intent? = null

    private lateinit var intentSenderLauncher: ActivityResultLauncher<IntentSenderRequest>
    private lateinit var intentSenderLauncher_move: ActivityResultLauncher<IntentSenderRequest>
    @SuppressLint("NotifyDataSetChanged", "NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        intentSenderLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // Retry the rename operation
                pendingFilePath?.let { filePath ->
                    pendingNewName?.let { newName ->
                        if (!renameFileScopedStorage(this, filePath, newName)) {
                            Toast.makeText(this, "File renaming failed.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Failed to get user permission", Toast.LENGTH_SHORT).show()
            }
            // Clear pending data
            pendingFilePath = null
            pendingNewName = null
        }
        intentSenderLauncher_move = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                // After permission is granted, navigate to the folder activity
                pendingIntent?.let {
                    startActivity(it)
                }
            } else {
                Toast.makeText(this, "Failed to get user permission", Toast.LENGTH_SHORT).show()
            }
            // Clear pending data
            pendingFilePath1 = null
            pendingIntent = null
        }

        binding.img111.setOnClickListener {
            binding.img111.setImageResource(R.drawable.baseline_heart_broken_24)
        }
        val data = intent.getStringExtra("img")
        val position = intent.getIntExtra("position", 0)
        val imageList = intent.getStringArrayListExtra("imgList") as ArrayList<Model_Img>
        val clickImg = object : click_img {
            override fun click(img: Model_Img) {
                Log.e("TAG_DATA223", "click: >>>>>>>>>>>>> ${adapter.set_type()}", )
                click=!click
                if (click){
                    binding.cl.visibility=View.GONE
                    binding.ll12.visibility=View.GONE
                }else{
                    binding.cl.visibility=View.VISIBLE
                    binding.ll12.visibility=View.VISIBLE
                }

                Log.e("PDF123", "click:>>>PDF#####${img.str}")
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
                    Log.e("TAG123dd", "click:>>>${img.str} ", )
                    createPdfWithImage(img.str)
//                    Log.e("PDF", "img:>>>$img")
//                    val pdfUri = convertImageToPdf(img.str)
//                    if (pdfUri != null) {
//                        Log.e("PDF", "uri:$pdfUri")
//                        openFileManagerForSaving(pdfUri)
//                    } else {
//                        Toast.makeText(this@MainActivity_data, "Failed to convert image to PDF", Toast.LENGTH_SHORT).show()
//                    }
                    //  FileUtil.initialize(intentSenderLauncher);


                }

            }
        }

        binding.buttonRename.setOnClickListener {
            val img = imageList[binding.recyclerView.currentItem]
            Log.e("TAG_54", "onCreate:>>$img ")
            Log.e("TAG_54", "img.str:>>${img.str} ")

            val filePath = img.str

            val dialog = AlertDialog.Builder(this@MainActivity_data)
            dialog.setTitle("Rename File")
            dialog.setMessage("Enter new name for the file")

            val input = EditText(this@MainActivity_data)
            dialog.setView(input)

            dialog.setPositiveButton("Rename") { _, _ ->
                val newName = input.text.toString()
                if (!renameFile(this@MainActivity_data, filePath, newName)) {
                    Toast.makeText(this@MainActivity_data, "File renaming failed.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@MainActivity_data, "File renamed successfully!", Toast.LENGTH_SHORT).show()
                }
            }

            dialog.setNegativeButton("Cancel") { _, _ -> }

            dialog.show()
        }
            binding.buttonMove.setOnClickListener {


//                val intent: Intent = Intent(this@MainActivity_data, Activity_Foulder::class.java)
//                intent.putExtra("IMAGE_URL", filePath)
//                intent.putExtra("FLAG", true)
//                startActivity(intent)

                val img = imageList[binding.recyclerView.currentItem]
                val filePath = img.str

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    // Request permission to modify the file before moving it
                    requestPermissionThenMoveFile(filePath)
                } else {

                    moveToFolder(filePath)
                }

//                val file=File(filePath)
//                val fileType = getMimeType(file)
//                val intent = Intent(this, Activity_Foulder::class.java)
//                intent.putExtra("filePath", filePath)
//                intent.putExtra("fileType", fileType)
//                startActivityForResult(intent, MOVE_FILE_REQUEST_CODE)


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
        val currentType = adapter.set_type()

        Log.e("TYPE", "currentType:>>$currentType ", )
        Log.e("TYPE", "currentType:>>${adapter.str_type} ", )
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
        val file = File(filePath)
        val mimeType = getMimeType(file)
        val collection = when (mimeType) {
            "image/*" -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            "video/*" -> MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else -> return false
        }

        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DATA}=?"
        val selectionArgs = arrayOf(filePath)

        val cursor: Cursor? = context.contentResolver.query(collection, projection, selection, selectionArgs, null)
        if (cursor != null && cursor.moveToFirst()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
            val fileUri = Uri.withAppendedPath(collection, id.toString())

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, newName)
            }

            return try {
                val updated = context.contentResolver.update(fileUri, contentValues, null, null)
                if (updated > 0) {
                    Log.d("FileUtil", "File renamed successfully to $newName")
                    true
                } else {
                    Log.e("FileUtil", "File renaming failed")
                    false
                }
            } catch (e: RecoverableSecurityException) {
                val intentSender: IntentSender = e.userAction.actionIntent.intentSender
                pendingFilePath = filePath
                pendingNewName = newName
                intentSenderLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
                false
            } catch (e: Exception) {
                Log.e("FileUtil", "File renaming failed: ${e.message}")
                false
            } finally {
                cursor.close()
            }
        } else {
            Log.e("FileUtil", "File not found")
            cursor?.close()
            return false
        }
    }


    private fun renameFileLegacy(filePath: String, newName: String): Boolean {
        val oldFile = File(filePath)
        Log.e("OLDFILE", "renameFileLegacy:$oldFile ")
        val extension = oldFile.name.substring(oldFile.name.lastIndexOf("."))
        Log.e("OLDFILE", "renameFileLegacy:$extension ")
        val newFileName = "$newName$extension"
        Log.e("OLDFILE", "renameFileLegacy:$newFileName ")
        val newFile = File(oldFile.parent, newFileName)
        Log.e("OLDFILE", "renameFileLegacy:$newFile ")
        return if (oldFile.renameTo(newFile)) {
            Log.e("OLDFILE", "if>>>>>>>>>>>>>>>>>>>>>>>>>>>>:${oldFile.renameTo(newFile)} ")
            Log.d("FileUtil", "File renamed successfully to ${newFile.absolutePath}")
            true
        } else {
            Log.e("OLDFILE", "else>>>>>>>>>>>>>>>>>>>>>>>>>>>>:${oldFile.renameTo(newFile)} ")
            Log.e("FileUtil", "File renaming failed")
            false
        }
    }

    private fun getMimeType(file: File): String {
        val extension = file.name.substring(file.name.lastIndexOf("."))
        return when (extension) {
            ".jpg", ".jpeg", ".png", ".gif", ".bmp" -> "image/*"
            ".mp4", ".avi", ".mkv", ".mov" -> "video/*"
            else -> "*/*"
        }
    }



    private fun requestPermissionThenMoveFile(filePath: String) {
        val file = File(filePath)
        val mimeType = getMimeType(file)
        val collection = when (mimeType) {
            "image/*" -> MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            "video/*" -> MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else -> return
        }

        val projection = arrayOf(MediaStore.MediaColumns._ID)
        val selection = "${MediaStore.MediaColumns.DATA}=?"
        val selectionArgs = arrayOf(filePath)

        val cursor: Cursor? = contentResolver.query(collection, projection, selection, selectionArgs, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val fileUri = Uri.withAppendedPath(collection, id.toString())

                try {
                    val contentValues = ContentValues()
                    contentResolver.update(fileUri, contentValues, null, null)
                    moveToFolder(filePath)
                } catch (e: RecoverableSecurityException) {
                    val intentSender: IntentSender = e.userAction.actionIntent.intentSender
                    pendingFilePath1 = filePath

                    // Prepare the intent for Activity_Foulder
                    val intent = Intent(this, Activity_Foulder::class.java)
                    intent.putExtra("filePath", filePath)
                    intent.putExtra("fileType", mimeType)
                    pendingIntent = intent

                    intentSenderLauncher_move.launch(IntentSenderRequest.Builder(intentSender).build())
                }
            }
        }
    }

    private fun moveToFolder(filePath: String) {
        val file = File(filePath)
        val fileType = getMimeType(file)

        // Intent to start Activity_Foulder
        val intent = Intent(this, Activity_Foulder::class.java)
        intent.putExtra("filePath", filePath)
        intent.putExtra("fileType", fileType)
        intent.putExtra("FLAG",true)
        startActivity(intent)
    }


//    private fun createPdfWithImage(IMAGE_PATH:String) {
//        val pdfDocument = PdfDocument()
//        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
//        val page = pdfDocument.startPage(pageInfo)
//        val canvas: Canvas = page.canvas
//
//        try {
//            // Load the image from the file
//            val imageFile = File(IMAGE_PATH)
//            if (imageFile.exists()) {
//                val bitmap = BitmapFactory.decodeFile(IMAGE_PATH)
//
//                // Draw the image on the PDF
//                canvas.drawBitmap(bitmap, 0f, 0f, null)
//
//                // Optional: Draw text on the PDF
////                val paint = Paint().apply {
////                    color = Color.BLACK
////                    textSize = 20f
////                    isAntiAlias = true
////                }
////               // canvas.drawText("Image added to PDF", 80f, 50f, paint)
//
//                pdfDocument.finishPage(page)
//
//                // Save the PDF file
//                val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), PDF_FILE_NAME)
//                pdfDocument.writeTo(FileOutputStream(file))
//                Toast.makeText(this, "PDF created successfully at ${file.absolutePath}", Toast.LENGTH_SHORT).show()
//
//                // Print the PDF
//                printPdf(file)
//            } else {
//                Toast.makeText(this, "Image file does not exist.", Toast.LENGTH_SHORT).show()
//            }
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
//        } finally {
//            pdfDocument.close()
//        }
//    }

    private fun printPdf(file: File) {
        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
        val printAdapter = object : PrintDocumentAdapter() {
            override fun onLayout(
                oldAttributes: PrintAttributes?,
                newAttributes: PrintAttributes?,
                cancellationSignal: android.os.CancellationSignal?,
                callback: PrintDocumentAdapter.LayoutResultCallback?,
                extras: Bundle?
            ) {
                callback?.onLayoutFinished(
                    PrintDocumentInfo.Builder(PDF_FILE_NAME).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build(),
                    oldAttributes == newAttributes
                )
            }

            override fun onWrite(
                pages: Array<out PageRange>?,
                destination: ParcelFileDescriptor,
                cancellationSignal: android.os.CancellationSignal?,
                callback: PrintDocumentAdapter.WriteResultCallback?
            ) {
                try {
                    FileInputStream(file).use { input ->
                        FileOutputStream(destination.fileDescriptor).use { output ->
                            val buffer = ByteArray(1024)
                            var length: Int
                            while (input.read(buffer).also { length = it } > 0) {
                                output.write(buffer, 0, length)
                            }
                            callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    callback?.onWriteFailed(e.message)
                }
            }
        }
        printManager.print("Document", printAdapter, null)
    }
    private fun createPdfWithImage(IMAGE_PATH: String) {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas: Canvas = page.canvas

        try {
            // Load the image from the file
            val imageFile = File(IMAGE_PATH)
            if (imageFile.exists()) {
                val bitmap = BitmapFactory.decodeFile(IMAGE_PATH)

                // Get the size of the PDF page and the image
                val pdfWidth = pageInfo.pageWidth
                val pdfHeight = pageInfo.pageHeight
                val imageWidth = bitmap.width
                val imageHeight = bitmap.height

                // Calculate the scale and position for center-crop
                val scale = maxOf(pdfWidth / imageWidth.toFloat(), pdfHeight / imageHeight.toFloat())
                val scaledWidth = imageWidth * scale
                val scaledHeight = imageHeight * scale
                val left = (pdfWidth - scaledWidth) / 2
                val top = (pdfHeight - scaledHeight) / 2

                // Draw the image on the PDF, center-cropped
                val destRect = RectF(left, top, left + scaledWidth, top + scaledHeight)
                canvas.drawBitmap(bitmap, null, destRect, null)

                pdfDocument.finishPage(page)

                // Save the PDF file
                val file = File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), PDF_FILE_NAME)
                pdfDocument.writeTo(FileOutputStream(file))
                Toast.makeText(this, "PDF created successfully at ${file.absolutePath}", Toast.LENGTH_SHORT).show()

                // Print the PDF
                printPdf(file)
            } else {
                Toast.makeText(this, "Image file does not exist.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error creating PDF: ${e.message}", Toast.LENGTH_LONG).show()
        } finally {
            pdfDocument.close()
        }
    }

}