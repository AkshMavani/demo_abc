package com.example.abc

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
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
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.demo_full.R
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.OutputStream

class ActivityPrinter : AppCompatActivity() {

//    private val PERMISSION_REQUEST_CODE = 1001
//    private val IMAGE_PATH = "/storage/emulated/0/Pictures/IMG_20240901_112133.jpg"
//    private val PDF_FILE_NAME = "HD_Camera.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_printer)
        val img=findViewById<ImageView>(R.id.img)
        val latestImageUri = getLatestImageFromCameraFolder(this)
        if (latestImageUri != null) {
            Log.d("GalleryActivity", "Latest image URI: $latestImageUri")
            img.setImageURI(latestImageUri)
            // Use the URI as needed, e.g., display the image in an ImageView
        } else {
            Log.d("GalleryActivity", "No images found in the Camera folder.")
        }


//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), PERMISSION_REQUEST_CODE)
//        } else {
//            createPdfWithImage(IMAGE_PATH)
//        }
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
//
//    private fun printPdf(file: File) {
//        val printManager = getSystemService(PRINT_SERVICE) as PrintManager
//        val printAdapter = object : PrintDocumentAdapter() {
//            override fun onLayout(
//                oldAttributes: PrintAttributes?,
//                newAttributes: PrintAttributes?,
//                cancellationSignal: android.os.CancellationSignal?,
//                callback: PrintDocumentAdapter.LayoutResultCallback?,
//                extras: Bundle?
//            ) {
//                callback?.onLayoutFinished(
//                    PrintDocumentInfo.Builder(PDF_FILE_NAME).setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT).build(),
//                    oldAttributes == newAttributes
//                )
//            }
//
//            override fun onWrite(
//                pages: Array<out PageRange>?,
//                destination: ParcelFileDescriptor,
//                cancellationSignal: android.os.CancellationSignal?,
//                callback: PrintDocumentAdapter.WriteResultCallback?
//            ) {
//                try {
//                    FileInputStream(file).use { input ->
//                        FileOutputStream(destination.fileDescriptor).use { output ->
//                            val buffer = ByteArray(1024)
//                            var length: Int
//                            while (input.read(buffer).also { length = it } > 0) {
//                                output.write(buffer, 0, length)
//                            }
//                            callback?.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    callback?.onWriteFailed(e.message)
//                }
//            }
//        }
//        printManager.print("Document", printAdapter, null)
//    }
//
//    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //createPdfWithImage()
//            } else {
//                Toast.makeText(this, "Permission denied.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }


    private fun getLatestImageFromCameraFolder(context: Context): Uri? {
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.DATE_TAKEN
        )

        val selection = "${MediaStore.Images.Media.BUCKET_DISPLAY_NAME} = ?"
        val selectionArgs = arrayOf("Pictures")

        val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        var cursor: Cursor? = null
        var latestImageUri: Uri? = null

        try {
            cursor = context.contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                sortOrder
            )

            if (cursor != null && cursor.moveToFirst()) {
                // Get the ID of the latest image
                val idColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                val imageId = cursor.getLong(idColumnIndex)
                latestImageUri = Uri.withAppendedPath(uri, imageId.toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
        }

        return latestImageUri
    }
}
