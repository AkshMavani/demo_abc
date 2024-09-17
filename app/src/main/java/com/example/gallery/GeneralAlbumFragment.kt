package com.example.gallery

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.demo_full.R
import com.example.demo_full.databinding.FragmentGeneralAlbumBinding
import com.example.gallery.ui.MediaViewModel
import com.example.gallery.ui.model.AlbumDetail

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GeneralAlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GeneralAlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
   // var albumsGeneralAdapter: AlbumsGeneralAdapter? = null
    var homeViewModel: MediaViewModel? = null
    var galleryModelList: List<AlbumDetail?> = ArrayList()
    var backStateName = javaClass.name
    private var param2: String? = null
    private lateinit var binding:FragmentGeneralAlbumBinding

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
        binding = FragmentGeneralAlbumBinding.inflate(inflater, container, false)
      //  homeViewModel = ViewModelProvider(this).get(MediaViewModel::class.java)

//        homeViewModel.mAlbumModels.observe(viewLifecycleOwner) { albumModels ->
//            m256xb6d340bb(albumModels)
//        }
//
//        homeViewModel.mGalleryModels.observe(viewLifecycleOwner) { galleryModels ->
//            binding.tvTotal1.text = AlbumUtil.getAlbumScreenShots(galleryModels).size.toString()
//        }
//
//        homeViewModel.mGalleryModelsVideo.observe(viewLifecycleOwner) { galleryModels ->
//            if (galleryModels != null) {
//                binding.tvTotal.text = homeViewModel.mGalleryModelsVideo.value?.size.toString()
//            }
//        }
//
//        homeViewModel.mGalleryModelsRecent.observe(viewLifecycleOwner) { galleryModels ->
//            if (galleryModels != null) {
//                binding.tvTotal2.text = homeViewModel.mGalleryModelsRecent.value?.size.toString()
//            }
//        }

        binding.a1.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putInt(a3.a.e, 1)
            val imageAlbumFragment = ImageAlbumFragment()
//            imageAlbumFragment.arguments = bundle
            requireFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.right_in_os14, R.anim.left_out_os14, R.anim.left_in_os14, R.anim.right_out_os14)
                .setReorderingAllowed(true)
                .add(R.id.container_album, imageAlbumFragment)
                .addToBackStack(backStateName)
                .commit()
        }

        binding.a2.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putInt(a3.a.e, 2)
            val imageAlbumFragment = ImageAlbumFragment()
         //   imageAlbumFragment.arguments = bundle
            fragmentManager?.beginTransaction()
                ?.setCustomAnimations(R.anim.right_in_os14, R.anim.left_out_os14, R.anim.left_in_os14, R.anim.right_out_os14)
                ?.setReorderingAllowed(true)
                ?.add(R.id.container_album, imageAlbumFragment)
                ?.addToBackStack(backStateName)
                ?.commit()
        }

        binding.a3.setOnClickListener {
            requireFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .setCustomAnimations(R.anim.right_in_os14, R.anim.left_out_os14, R.anim.left_in_os14, R.anim.right_out_os14)
                .add(R.id.container_album, RecentDeleteFragment())
                .addToBackStack(backStateName)
                .commit()
        }

        binding.imgAddAlbums.setOnClickListener {
            //m257x43c057da(it)
        }

        binding.tvSeeAll.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.setCustomAnimations(R.anim.right_in_os14, R.anim.left_out_os14, R.anim.left_in_os14, R.anim.right_out_os14)
                ?.add(R.id.container_album, DetailAlbumFragment())
                ?.addToBackStack(backStateName)
                ?.commit()
        }
        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GeneralAlbumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GeneralAlbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}