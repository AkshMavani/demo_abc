package com.example.abc

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.PointF
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo_full.R
import com.example.demo_full.databinding.ActivityCamera1Binding
import com.example.filte.FilterAdapter
import com.example.filte.Model_Filter
import com.otaliastudios.cameraview.CameraException
import com.otaliastudios.cameraview.CameraListener
import com.otaliastudios.cameraview.CameraOptions
import com.otaliastudios.cameraview.CameraView
import com.otaliastudios.cameraview.PictureResult
import com.otaliastudios.cameraview.VideoResult
import com.otaliastudios.cameraview.filter.Filters
import com.otaliastudios.cameraview.size.AspectRatio
import com.otaliastudios.cameraview.size.SizeSelectors
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream


class Activity_Camera1 : AppCompatActivity() {
    private lateinit var cameraView: CameraView
    var arr:ArrayList<Model_Filter> = ArrayList()
    private var captureTime: Long = 0

    private var currentFilter = 0
    private lateinit var binding:ActivityCamera1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityCamera1Binding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cameraView12.addCameraListener(Listener())
        binding.button.setOnClickListener { binding.cameraView12.takePicture()

        }
        cameraView = findViewById(R.id.cameraView12)
        cameraView.setLifecycleOwner(this)


        Log.e("camera12", "onCreate:>>>${cameraView.filter} ")
        Log.e("camera12", "onCreate:>>>${cameraView.filter} ")
         val allFilters =
             Filters.values()
        for (i in allFilters){
            Log.e("TAG123", "onCreate:>>>>${i.name} ")
            arr.add(Model_Filter(i))

        }
       val HorizontalLayout = LinearLayoutManager(
            this@Activity_Camera1,
            LinearLayoutManager.HORIZONTAL,
            false
        )
       binding.recycleviewFilter.setLayoutManager(HorizontalLayout)
        val adapter=FilterAdapter(arr)
        binding.recycleviewFilter.adapter=adapter
//        cameraView.addFrameProcessor(object : FrameProcessor {
//            override fun process(frame: Frame) {
//                Log.e("Process12", "formate:>>${frame.format} ")
//                Log.e("Process12", "frame:>>${frame} ")
//                Log.e("Process12", "rotation:>>${frame.rotation} ")
//                Log.e("Process12", "rotation user:>>${frame.rotationToUser} ")
//                Log.e("Process12", "rotationview:>>${frame.rotationToView} ")
//                Log.e("Process12", "size:>>${frame.size} ")
//                Log.e("Process12", "frezz:>>${frame.freeze()} ")
//                Log.e("Process12", "relse:>>${frame.release()} ")
//                Log.e("Process12", "relse:>>${frame} ")
//            }
//        })




    }
  private inner class Listener : CameraListener() {
        override fun onCameraOpened(options: CameraOptions) {
        }

        override fun onCameraError(exception: CameraException) {
            super.onCameraError(exception)

        }

        override fun onPictureTaken(result: PictureResult) {
            super.onPictureTaken(result)
            result.toBitmap { bitmap ->
                binding.cameraView12.visibility= View.GONE
                binding.img.visibility= View.VISIBLE
                binding.img.setImageBitmap(bitmap)
                if (bitmap != null) {
                    saveBitmapToExternalStorage(bitmap,"hlooo")
                }
                val callbackTime = System.currentTimeMillis()
                if (captureTime == 0L) captureTime = callbackTime - 300

                startActivity(intent)
                captureTime = 0
            }
        }

        override fun onVideoTaken(result: VideoResult) {
            super.onVideoTaken(result)

        }

        override fun onVideoRecordingStart() {
            super.onVideoRecordingStart()

        }

        override fun onVideoRecordingEnd() {
            super.onVideoRecordingEnd()

        }

        override fun onExposureCorrectionChanged(newValue: Float, bounds: FloatArray, fingers: Array<PointF>?) {
            super.onExposureCorrectionChanged(newValue, bounds, fingers)

        }

        override fun onZoomChanged(newValue: Float, bounds: FloatArray, fingers: Array<PointF>?) {
            super.onZoomChanged(newValue, bounds, fingers)

        }
    }
    fun saveBitmapToExternalStorage(bitmap: Bitmap, fileName: String) {
        val filename = "$fileName.jpg"
        var fos: OutputStream? = null

        // For devices running Android Q and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }

            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                fos = resolver.openOutputStream(imageUri)
            }
        } else { // For devices running below Android Q
            val imagesDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()
            val image = File(imagesDir, filename)
            fos = FileOutputStream(image)
        }

        fos?.use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
            Log.d("SaveImage", "Image saved successfully")
        }
    }
}
