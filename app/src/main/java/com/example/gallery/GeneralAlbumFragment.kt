package com.example.gallery


import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R
import com.example.demo_full.databinding.FragmentGeneralAlbumBinding
import com.example.gallery.ui.MediaViewModel
import com.example.gallery.ui.adapter.AlbumsGeneralAdapter
import com.example.gallery.ui.adapter.DialogCreateAlbum
import com.example.gallery.ui.model.AlbumDetail
import com.example.gallery.util.Video_Recently_Utils


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
    lateinit var albumsGeneralAdapter: AlbumsGeneralAdapter

   // var albumsGeneralAdapter: AlbumsGeneralAdapter? = null
    var homeViewModel: MediaViewModel? = null
    var galleryModelList: ArrayList<AlbumDetail?> = ArrayList()

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
        Video_Recently_Utils.GalleryImagesTrash(requireContext())
        Video_Recently_Utils.GalleryVideo(requireContext())
        Video_Recently_Utils.getListAlbum(requireContext())
        initlist()
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
                .addToBackStack(null).commit()
        }
        binding.imgAddAlbums.setOnClickListener {
            DialogCreateAlbum(this, homeViewModel).showPictureialog()
//            val listPopupWindow = ListPopupWindow(requireContext())
//            listPopupWindow.setAdapter(popUpAdapter)
//            listPopupWindow.setAnchorView(itemView)
//            listPopupWindow.setDropDownGravity(Gravity.NO_GRAVITY)
//            listPopupWindow.setBackgroundDrawable(
//                ContextCompat.getDrawable(
//                    requireContext(),
//                    R.drawable.round_corner
//                )
//            )
//            listPopupWindow.setWidth(ListPopupWindow.WRAP_CONTENT)
//            listPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT)
//            listPopupWindow.show()
        }
        binding.a2.setOnClickListener {
//            val bundle = Bundle()
//            bundle.putInt(a3.a.e, 2)
            val navController = findNavController()
            navController.navigate(R.id.action_navigation_general_iamge_to_navigation_album_iamge)





        }

        binding.a3.setOnClickListener {

            val navController = findNavController()
            navController.navigate(R.id.action_navigation_general_iamge_to_navigation_recent_delete)
        }

        binding.imgAddAlbums.setOnClickListener {
            //m257x43c057da(it)


        }

        binding.tvSeeAll.setOnClickListener {
            fragmentManager?.beginTransaction()
                ?.setReorderingAllowed(true)
                ?.setCustomAnimations(R.anim.right_in_os14, R.anim.left_out_os14, R.anim.left_in_os14, R.anim.right_out_os14)
                ?.add(R.id.container_album, DetailAlbumFragment())
                ?.addToBackStack(null)
                ?.commit()
        }
        return binding.root
    }

    private fun initlist() {
        val list=lambdagetListAlbum(requireContext())
        Log.e("LIST123", "initlist: >>>>$list")
        albumsGeneralAdapter = AlbumsGeneralAdapter(
            homeViewModel, list,
            context, object : AlbumsGeneralAdapter.OnClickCustom {
                // from class: com.example.iphoto.ui.notifications.GeneralAlbumFragment.8
                // com.example.iphoto.adapter.AlbumsGeneralAdapter.OnClickCustom
                override fun remove(albumDetail: AlbumDetail?) {}

                // com.example.iphoto.adapter.AlbumsGeneralAdapter.OnClickCustom
                @SuppressLint("UseRequireInsteadOfGet")
                override fun onCLick1(albumDetail: AlbumDetail) {
                    val bundle = Bundle()
                    bundle.putString("budget_name", albumDetail.buget_name)
                    bundle.putBoolean("isFolder", albumDetail.isFolder)

                    val navController = findNavController()
                    navController.navigate(R.id.action_navigation_general_iamge_to_navigation_album_iamge,bundle)
                }
            })
        binding.rcvAlbums.adapter = this.albumsGeneralAdapter
        binding.rcvAlbums.layoutManager =
            GridLayoutManager(context, 2, RecyclerView.HORIZONTAL, false)
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

     fun m256xb6d340bb(list: List<AlbumDetail>) {
        if (list == null) {
            return
        }
        galleryModelList.clear()
        galleryModelList.addAll(list)
        initlist()
    }

    @SuppressLint("Range")
    @Throws(Throwable::class)
    fun  lambdagetListAlbum(
        context: Context,
    ):ArrayList<AlbumDetail> {
        val contentUri: Uri
        val arrayList: java.util.ArrayList<AlbumDetail> = java.util.ArrayList<AlbumDetail>()
        try {

            val bundle = Bundle()
            bundle.putString("android:query-arg-sql-sort-order", "date_modified DESC")
            bundle.putString("android:query-arg-sql-group-by", "bucket_display_name")
            contentUri = if (Build.VERSION.SDK_INT >= 30) {
                MediaStore.Files.getContentUri("external_primary")
            } else {
                MediaStore.Files.getContentUri("external")
            }
            val query = if (Build.VERSION.SDK_INT >= 26) context.contentResolver.query(
                contentUri,
                null,
                bundle,
                null
            ) else null
            val count = query!!.count
            for (i in 0 until count) {
                val albumDetail = AlbumDetail()
                query.moveToPosition(i)
                val string = query.getString(query.getColumnIndex("_data"))
                val string2 = query.getString(query.getColumnIndex("bucket_display_name"))
                val i2 = query.getInt(query.getColumnIndex("bucket_id"))
                albumDetail.path = string
                albumDetail.buget_name = string2
                albumDetail.bugetId = i2.toLong()
                albumDetail.count =getCount(context, string2)
                if (string.contains("mp4") || string.contains("jpeg") || string.contains("jpg") || string.contains(
                        "png"
                    )
                ) {
                    arrayList.add(albumDetail)
                }
            }
            query.close()
        } catch (e: Exception) {
            e.message
        }
        return arrayList
    }
    fun getCount(context: Context, bucketId: String): Int {
        var i = 0
        val query = context.contentResolver.query(
            MediaStore.Files.getContentUri("external"),
            null,
            "bucket_display_name=?",
            arrayOf(bucketId),
            null
        )
        if (query != null) {
            try {
                if (query.moveToFirst()) {
                    i = query.count
                }
            } catch (th: Throwable) {
                if (query != null) {
                    try {
                        query.close()
                    } catch (th2: Throwable) {
                        th.addSuppressed(th2)
                    }
                }
                throw th
            }
        }
        query?.close()
        return i
    }
}