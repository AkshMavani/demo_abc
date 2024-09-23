package com.example.demo_full

import android.content.Context
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.camera.Click_image
import com.example.demo_full.databinding.FragmentFragmentgalley1Binding
import com.example.demo_full.databinding.FragmentGallery2Binding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Fragmentgalley1.newInstance] factory method to
 * create an instance of this fragment.
 */
class Fragmentgalley1 : Fragment(),Click_image {
    lateinit var _binding: FragmentFragmentgalley1Binding
    private val binding get() = _binding!!
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFragmentgalley1Binding.inflate(inflater, container, false)
       binding.rc.setHasFixedSize(true);
        binding.rc.layoutManager = GridLayoutManager(requireContext(), 3)
        val adapter=ImageAdapter12(requireContext(),
            getAllMedia(requireContext()) as ArrayList<Model_Img>,this)
        binding.rc.adapter=adapter
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Fragmentgalley1.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Fragmentgalley1().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    fun getAllMedia(context: Context): List<Model_Img> {
        val mediaList = ArrayList<Model_Img>()

        // Query for images
        val imageProjection = arrayOf(
            MediaStore.Images.Media.DATA,              // File path
            MediaStore.Images.Media.DATE_TAKEN         // Date taken
        )

        val imageSortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

        val imageCursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            imageProjection,
            null, // No selection, fetch all images
            null, // No selection args
            imageSortOrder
        )

        imageCursor?.use {
            val imagePathColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val imagePath = it.getString(imagePathColumn)
                val dateTaken = it.getLong(dateTakenColumn) // Get date taken for the image
                mediaList.add(Model_Img(imagePath, "IMG", dateTaken))
            }
        }

        // Query for videos
        val videoProjection = arrayOf(
            MediaStore.Video.Media.DATA,              // File path
            MediaStore.Video.Media.DATE_TAKEN         // Date taken
        )

        val videoSortOrder = "${MediaStore.Video.Media.DATE_TAKEN} DESC"

        val videoCursor = context.contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            videoProjection,
            null, // No selection, fetch all videos
            null, // No selection args
            videoSortOrder
        )

        videoCursor?.use {
            val videoPathColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)
            val dateTakenColumn = it.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_TAKEN)

            while (it.moveToNext()) {
                val videoPath = it.getString(videoPathColumn)
                val dateTaken = it.getLong(dateTakenColumn) // Get date taken for the video
                mediaList.add(Model_Img(videoPath, "VIDEO", dateTaken))
            }
        }

        return mediaList
    }

    override fun click(modelImg: Model_Img) {

    }

}