package com.example.gallery.ui.adapter

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demo_full.R
import com.example.gallery.ui.model.GalleryModel
import java.io.File

class ImageAdapter(
    private val context: Context,
    private val imageList: List<GalleryModel>
) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val imageName: TextView = view.findViewById(R.id.imageName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_image13, parent, false)
        return this.ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val image = imageList[position]

        // Load the image into the ImageView using Glide
        Glide.with(context)
            .load(Uri.parse(image.uri))
            .into(holder.imageView)

        // Set the image name
        holder.imageName.text = image.path?.substringAfterLast("/")
        holder.itemView.setOnClickListener {
            recoverSingleImage(image.uri.toString(),context)
        }
    }

    override fun getItemCount(): Int = imageList.size
    private fun isImageFile(file: File): Boolean {
        val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp")
        val fileName = file.name.lowercase()
        return imageExtensions.any { fileName.endsWith(it) }
    }
    fun recoverSingleImage(imageUri: String,context: Context) {
        val file = File(Uri.parse(imageUri).path ?: "")

        if (!file.exists() || !file.isFile || !isImageFile(file)) {
            Toast.makeText(context, "Invalid image file.", Toast.LENGTH_SHORT).show()
            return
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Adjust based on the file type
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // Save to Pictures folder
        }

        val resolver = context.contentResolver
        val uri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        uri?.let {
            try {
                // Write the file content to the new location
                val outputStream = resolver.openOutputStream(it)
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream!!)
                }
                outputStream?.close()

                // Delete the file from the private folder after successful recovery
                if (file.delete()) {
                    Toast.makeText(context, "Image recovered to gallery.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to delete original image.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RecoverImage", "Failed to recover image: ${file.name}", e)
            }
        } ?: run {
            Log.e("RecoverImage", "Failed to get URI for recovering image: ${file.name}")
            Toast.makeText(context, "Failed to recover image.", Toast.LENGTH_SHORT).show()
        }
    }

}