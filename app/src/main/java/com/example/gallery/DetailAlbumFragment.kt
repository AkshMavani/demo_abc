package com.example.gallery

import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.deniz.draggablelibrary.DraggableFrameLayout
import com.example.demo_full.R
import com.example.demo_full.databinding.FragmentDetailAlbumBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DetailAlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DetailAlbumFragment : Fragment() {
   // private lateinit var binding:FragmentDetailAlbumBinding

    private var _binding: FragmentDetailAlbumBinding? = null

    // with the backing property of the kotlin we extract
    // the non null value of the _binding
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
      //  binding= FragmentDetailAlbumBinding.inflate(R.layout.fragment_detail_album, container, false)
        _binding = FragmentDetailAlbumBinding.inflate(inflater, container, false)
        val bundle = arguments
        if (bundle != null) {
            val locationOnScreen = bundle.getIntArray("locationOnScreen")
            val imageWidth = bundle.getInt("imageWidth")
            val imageHeight = bundle.getInt("imageHeight")
            val imagePath = bundle.getString("imagePath")

            // Use the retrieved values as needed
            Log.e("DetailImageFragment", "locationOnScreen: $locationOnScreen")
            Log.e("DetailImageFragment", "imageWidth: $imageWidth, imageHeight: $imageHeight")
            Log.e("DetailImageFragment", "imagePath: $imagePath")
        }

//        binding.root.setDragListener(object : DraggableFrameLayout.DragListener {
//            override fun onDragStarted(rawX: Float, rawY: Float) {
//                Log.e("TAG_Finish", "drag start: ", )
//            }
//
//            override fun onDrag(rawX: Float, rawY: Float, touchDeltaX: Float, touchDeltaY: Float) {
//                Log.e("TAG_Finish", "drag: ", )
//            }
//
//            override fun onDragFinishing(distance: Float) {
//                Log.e("TAG_Finish", "onDragFinishing:$distance ", )
//            }
//
//            override fun onDragFinished() {
//                Log.e("TAG_Finish", "onDragFinishing: ", )
//                val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//                navController.navigate(R.id.navigation_home)                // DO YOUR WORK HERE
//            }
//        })
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DetailAlbumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DetailAlbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}