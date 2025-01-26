package com.example.gallery

import android.annotation.SuppressLint
import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R
import com.example.gallery.ui.adapter.ImageAdapter
import com.example.gallery.ui.model.GalleryModel
import java.io.File

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_setting, container, false)
        val lst=getImagesFromAppFolder()
        Log.e("LST11", "onCreateView: $lst" )
      //  recoverAllImagesFromAppFolder()
        val images = getImagesFromAppFolder()
        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        // Set up the RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = ImageAdapter(requireContext(), images)
        return view.rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun getImagesFromAppFolder(): List<GalleryModel> {
        // Define the app's private "HiddenImages" folder
        val destDir = File(requireContext().getExternalFilesDir(null), "HiddenImages")

        // Check if the directory exists
        if (!destDir.exists()) {
            Log.e("FileOperation", "HiddenImages folder does not exist.")
            return emptyList() // Return an empty list if the folder doesn't exist
        }

        // List all image files in the folder
        val files = destDir.listFiles()
        val galleryModels = mutableListOf<GalleryModel>()

        files?.forEach { file ->
            if (file.isFile && isImageFile(file)) {
                // Map each file to a GalleryModel object
                galleryModels.add(
                    GalleryModel(
                        path = file.absolutePath,
                        uri = Uri.fromFile(file).toString(),
                        size = file.length(),
                        dateAdd = file.lastModified(),
                        bucketDisplayName = destDir.name,
                        width = 0, // You can calculate width if needed
                        height = 0, // You can calculate height if needed
                        typeMedia = 1 // Example: 1 for images, 2 for videos
                    )
                )
            }
        }

        return galleryModels
    }

    // Helper method to check if a file is an image
    private fun isImageFile(file: File): Boolean {
        val imageExtensions = listOf("jpg", "jpeg", "png", "gif", "bmp")
        val fileName = file.name.lowercase()
        return imageExtensions.any { fileName.endsWith(it) }
    }
    private fun recoverAllImagesFromAppFolder() {
        val hiddenImagesFolder = File(requireContext().getExternalFilesDir(null), "HiddenImages")

        // Check if the folder exists
        if (!hiddenImagesFolder.exists()) {
            Log.e("RecoverImages", "HiddenImages folder does not exist.")
            Toast.makeText(requireContext(), "No images to recover.", Toast.LENGTH_SHORT).show()
            return
        }

        // Get all image files from the folder
        val files = hiddenImagesFolder.listFiles()?.filter { isImageFile(it) } ?: emptyList()

        if (files.isEmpty()) {
            Toast.makeText(requireContext(), "No images to recover.", Toast.LENGTH_SHORT).show()
            return
        }

        files.forEach { file ->
            recoverImageToGallery(file)
        }

        // Notify the user
        Toast.makeText(requireContext(), "All images recovered to the gallery.", Toast.LENGTH_SHORT).show()
    }
    private fun recoverImageToGallery(file: File) {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Update MIME type based on the file type
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // Save to the Pictures folder
        }

        val resolver = requireContext().contentResolver
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
                file.delete()
            } catch (e: Exception) {
                Log.e("RecoverImage", "Failed to recover image: ${file.name}", e)
            }
        } ?: run {
            Log.e("RecoverImage", "Failed to get URI for recovering image: ${file.name}")
        }
    }

    fun recoverSingleImage(imageUri: String) {
        val file = File(Uri.parse(imageUri).path ?: "")

        if (!file.exists() || !file.isFile || !isImageFile(file)) {
            Toast.makeText(requireContext(), "Invalid image file.", Toast.LENGTH_SHORT).show()
            return
        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, file.name)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg") // Adjust based on the file type
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES) // Save to Pictures folder
        }

        val resolver = requireContext().contentResolver
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
                    Toast.makeText(requireContext(), "Image recovered to gallery.", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Failed to delete original image.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Log.e("RecoverImage", "Failed to recover image: ${file.name}", e)
            }
        } ?: run {
            Log.e("RecoverImage", "Failed to get URI for recovering image: ${file.name}")
            Toast.makeText(requireContext(), "Failed to recover image.", Toast.LENGTH_SHORT).show()
        }
    }

}