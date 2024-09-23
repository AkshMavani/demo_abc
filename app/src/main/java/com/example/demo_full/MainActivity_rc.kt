package com.example.demo_full

import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.demo_full.databinding.ActivityMainRcBinding
import java.io.OutputStream

class MainActivity_rc : AppCompatActivity() {
    private lateinit var binding: ActivityMainRcBinding
    private lateinit var layoutList: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainRcBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imgUrlList = intent.getSerializableExtra("img_url") as ArrayList<Model_Img>
        Log.e("IMGURLIST", "onCreate:$imgUrlList")

        // Load images based on the size of the imgUrlList
        when (imgUrlList.size) {
            2 -> {
                Glide.with(this).load(imgUrlList[0].str).into(binding.img1)
                Glide.with(this).load(imgUrlList[1].str).into(binding.img2)
            }
            3 -> {
                Glide.with(this).load(imgUrlList[0].str).into(binding.img1)
                Glide.with(this).load(imgUrlList[1].str).into(binding.img2)
                Glide.with(this).load(imgUrlList[2].str).into(binding.img3)
            }
            4 -> {
                Glide.with(this).load(imgUrlList[0].str).into(binding.img1)
                Glide.with(this).load(imgUrlList[1].str).into(binding.img2)
                Glide.with(this).load(imgUrlList[2].str).into(binding.img3)
                Glide.with(this).load(imgUrlList[3].str).into(binding.img4)
            }
            5 -> {
                Glide.with(this).load(imgUrlList[0].str).into(binding.img1)
                Glide.with(this).load(imgUrlList[1].str).into(binding.img2)
                Glide.with(this).load(imgUrlList[2].str).into(binding.img3)
                Glide.with(this).load(imgUrlList[3].str).into(binding.img4)
                Glide.with(this).load(imgUrlList[4].str).into(binding.img5)
            }
            6 -> {
                Glide.with(this).load(imgUrlList[0].str).into(binding.img1)
                Glide.with(this).load(imgUrlList[1].str).into(binding.img2)
                Glide.with(this).load(imgUrlList[2].str).into(binding.img3)
                Glide.with(this).load(imgUrlList[3].str).into(binding.img4)
                Glide.with(this).load(imgUrlList[4].str).into(binding.img5)
                Glide.with(this).load(imgUrlList[5].str).into(binding.img6)
            }
            7 -> {
                Glide.with(this).load(imgUrlList[0].str).into(binding.img1)
                Glide.with(this).load(imgUrlList[1].str).into(binding.img2)
                Glide.with(this).load(imgUrlList[2].str).into(binding.img3)
                Glide.with(this).load(imgUrlList[3].str).into(binding.img4)
                Glide.with(this).load(imgUrlList[4].str).into(binding.img5)
                Glide.with(this).load(imgUrlList[5].str).into(binding.img6)
                Glide.with(this).load(imgUrlList[6].str).into(binding.img7)
            }
        }

        // OnClickListener to save the layouts as images
        binding.save.setOnClickListener {
            when (imgUrlList.size) {
                2 -> {
                    layoutList = listOf(binding.clFirst, binding.clSecond)
                    SaveLayoutsTask().execute(*layoutList.toTypedArray())
                }
                3 -> {
                    layoutList = listOf(binding.clFirst, binding.clSecond, binding.clThird)
                    SaveLayoutsTask().execute(*layoutList.toTypedArray())
                }
                4 -> {
                    layoutList = listOf(binding.clFirst, binding.clSecond, binding.clThird, binding.clFourth)
                    SaveLayoutsTask().execute(*layoutList.toTypedArray())
                }
                5 -> {
                    layoutList = listOf(binding.clFirst, binding.clSecond, binding.clThird, binding.clFourth, binding.clFive)
                    SaveLayoutsTask().execute(*layoutList.toTypedArray())
                }
                6 -> {
                    layoutList = listOf(binding.clFirst, binding.clSecond, binding.clThird, binding.clFourth, binding.clFive, binding.clSix)
                    SaveLayoutsTask().execute(*layoutList.toTypedArray())
                }
                7 -> {
                    layoutList = listOf(binding.clFirst, binding.clSecond, binding.clThird, binding.clFourth, binding.clFive, binding.clSix, binding.clSeven)
                    SaveLayoutsTask().execute(*layoutList.toTypedArray())
                }
            }
        }
    }

    inner class SaveLayoutsTask : AsyncTask<View, Int, List<String?>>() {

        override fun onPreExecute() {
            super.onPreExecute()
            binding.progressBar.visibility = View.VISIBLE
        }

        override fun doInBackground(vararg views: View): List<String?> {
            val savedPaths = mutableListOf<String?>()
            for ((index, view) in views.withIndex()) {
                val result = saveLayoutToStorage(view, "layout${index + 1}")
                savedPaths.add(result)
                publishProgress((index + 1) * 100 / views.size) // Update progress
            }
            return savedPaths
        }

        override fun onProgressUpdate(vararg values: Int?) {
            super.onProgressUpdate(*values)
            binding.progressBar.progress = values[0] ?: 0 // Update the progress bar
        }

        override fun onPostExecute(result: List<String?>) {
            super.onPostExecute(result)
            binding.progressBar.visibility = View.GONE

            val savedMessage = result.filterNotNull().joinToString("\n") { path ->
                "Saved at: $path"
            }

            if (savedMessage.isNotEmpty()) {
                Toast.makeText(this@MainActivity_rc, savedMessage, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this@MainActivity_rc, "Error saving layouts", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Function to save a layout as an image using MediaStore
    private fun saveLayoutToStorage(view: View, layoutName: String): String? {
        val bitmap = getBitmapFromView(view)

        // Prepare values for saving the image to MediaStore
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "$layoutName.png")
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/SavedLayouts")
            }
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            var outputStream: OutputStream? = null
            return try {
                outputStream = resolver.openOutputStream(uri)
                outputStream?.let { it1 -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, it1) }
                uri.toString() // Return the URI string on success
            } catch (e: Exception) {
                Log.e("SAVE_LAYOUT", "Error saving layout: ${e.message}")
                null
            } finally {
                outputStream?.close()
            }
        }

        return null
    }

    // Function to convert a View to Bitmap
    private fun getBitmapFromView(view: View): Bitmap {
        val returnedBitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(returnedBitmap)
        val bgDrawable = view.background
        if (bgDrawable != null) {
            bgDrawable.draw(canvas)
        } else {
            canvas.drawColor(android.graphics.Color.WHITE)
        }
        view.draw(canvas)
        return returnedBitmap
    }
}
