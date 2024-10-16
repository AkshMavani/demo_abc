package com.example.gallery

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R
import com.example.demo_full.databinding.FragmentImageAlbumBinding
import com.example.gallery.DetailAlbumFragment.Companion.currentPosition
import com.example.gallery.ui.MediaViewModel
import com.example.gallery.ui.model.GalleryModel
import com.example.gallery.util.BottomSheetAddPhotoToAlbum
import com.example.gallery.util.Video_Recently_Utils.getAlbumNames

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ImageAlbumFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ImageAlbumFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var binding: FragmentImageAlbumBinding

    var MODE_EDIT = 0
    var MODE_SELECT = 1
    var folder = false
    var homeViewModel: MediaViewModel? = null

    var name: String? = null
    var type = 0
    var mGalleryModels: ArrayList<GalleryModel?> = ArrayList()
    var defaultMode = MODE_SELECT

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
        binding = FragmentImageAlbumBinding.inflate(inflater, container, false)
        if (arguments != null) {
            name = requireArguments().getString("budget_name")
            folder = requireArguments().getBoolean("isFolder")
        }
        binding.inMultiSelect.imgMore.visibility = View.GONE
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                // from class: com.example.iphoto.ui.ImageAlbumFragment.1
                // androidx.activity.OnBackPressedCallback
                override fun handleOnBackPressed() {
                    if (this@ImageAlbumFragment.fragmentManager != null) {
//                        FIleUtils.formatListgallery(mGalleryModels)
//                        (this@ImageAlbumFragment.activity as HomeActivity?).hideDelete()
//                        (this@ImageAlbumFragment.activity as HomeActivity?).getNavView()
//                            .setVisibility(0)
//                        this@ImageAlbumFragment.fragmentManager!!.popBackStackImmediate()
                    }
                }
            })

        this.homeViewModel?.galleryItemsLiveData?.observe(viewLifecycleOwner,
            Observer { galleryModelList ->
                if (galleryModelList.isNullOrEmpty()) {
                    binding.inNoPhoto.root.visibility = View.GONE
                    return@Observer
                }

                when (type) {
                    1 -> {
                        //    mGalleryModels = GalleryVideo(requireContext())
                        binding.tvAlbumName.text = context?.getString(R.string.video)
                    }

                    2 -> {
                        //    mGalleryModels = getAlbumScreenShots(galleryModelList)
                        binding.tvAlbumName.text = context?.getString(R.string.screenshot)
                    }

                    3 -> binding.tvAlbumName.text = context?.getString(R.string.recenlty)
                }

                name?.let {
                    mGalleryModels = getAlbumNames(galleryModelList, it) as ArrayList<GalleryModel?>
                    binding.tvAlbumName.text = it
                    mGalleryModels.add(GalleryModel())
                }

                if (mGalleryModels.isEmpty()) {
                    binding.tvRight.text = getString(R.string.add)
                    binding.inNoPhoto.root.visibility = View.VISIBLE
                } else {
                    binding.tvRight.text = getString(R.string.select_text)
                    binding.inNoPhoto.root.visibility = View.GONE
                }

                binding.rcvImage.layoutManager = GridLayoutManager(context, 2)
              //  initListData()
            })

        binding.tvLeft.setOnClickListener { view ->
            //m241lambda$onCreateView$0$comexampleiphotouiImageAlbumFragment(view)
        }

        binding.tvRight.setOnClickListener {
//            if (binding.tvRight.text == getString(R.string.add)) {
//                bottomSheetAddPhotoToAlbum = BottomSheetAddPhotoToAlbum(this, name)
//                bottomSheetAddPhotoToAlbum.show(childFragmentManager, tag)
//            }
//
//            if (defaultMode == MODE_SELECT) {
//                defaultMode = MODE_EDIT
//                binding.inMultiSelect.root.visibility = View.VISIBLE
//                (activity as HomeActivity).navView.visibility = View.GONE
//                binding.tvRight.text = getString(R.string.cancel)
//                mAdapter?.setChoose(true)
//            } else if (defaultMode == MODE_EDIT) {
//                binding.inMultiSelect.root.visibility = View.GONE
//                defaultMode = MODE_SELECT
//                (activity as HomeActivity).hideDelete()
//                (activity as HomeActivity).navView.visibility = View.VISIBLE
//                binding.tvRight.text = getString(R.string.select_text)
//                mAdapter?.setChoose(false)
//                FIleUtils.formatListgallery(mGalleryModels)
//            }
//
//            mAdapter?.notifyDataSetChanged()
        }

        binding.inMultiSelect.imgDelete.setOnClickListener { view ->
            //m242lambda$onCreateView$1$comexampleiphotouiImageAlbumFragment(view)
        }

        binding.inMultiSelect.imgShare.setOnClickListener {
            val arrayList = ArrayList<Parcelable>()
            mGalleryModels.forEach { galleryModel ->
//                if (galleryModel.isChoose) {
//                    if (galleryModel != null) {
//                        arrayList.add(FileProvider.getUriForFile(requireContext(), "com.photos.gallery.photoeditor.provider",
//                            File(galleryModel.path)))
//                    }
//                }
            }

            val intent = Intent().apply {
                action = Intent.ACTION_SEND_MULTIPLE
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, arrayList)
                type = "image/* video/*"
            }
            startActivity(Intent.createChooser(intent, null))
        }

//        binding.dragLayout.setDragListener(object : DragHorizontalLayout.DragListener {
//            override fun onDrag(x: Float, y: Float) {
//            }
//
//            override fun onDragStarted(x: Float, y: Float) {
//            }
//
//            override fun onDragFinished(x: Float, y: Float) {
//                if (x > Util.getScreenWidth() / 2) {
//                    binding.root.visibility = View.GONE
//                    FIleUtils.formatListgallery(mGalleryModels)
//                    (activity as HomeActivity).hideDelete()
//                    (activity as HomeActivity).navView.visibility = View.VISIBLE
//                    fragmentManager?.popBackStackImmediate()
//                }
//            }
//        })

        binding.rcvImage.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                recyclerView.computeVerticalScrollOffset()
            }
        })

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ImageAlbumFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ImageAlbumFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

//    fun initListData() {
//      val mAdapter = PhotoPreviewAlbumAdapter(homeViewModel, this, mGalleryModels, object : OnClickCuston() {
//                // from class: com.example.iphoto.ui.ImageAlbumFragment.7
//                // com.example.iphoto.adapter.PhotoPreviewAlbumAdapter.OnClickCuston
//                fun updatetvDateTime(pos: Int) {}
//
//                // com.example.iphoto.adapter.PhotoPreviewAlbumAdapter.OnClickCuston
//                @SuppressLint("UseRequireInsteadOfGet")
//                fun onCLick1(galleryModel: Int, view: View) {
//
//                        fun onAction() {
//                            currentPosition = galleryModel
//                            val imageView =
//                                view.findViewById<View>(R.id.preview_image_view) as ImageView
//                            val iArr = IntArray(2)
//                            imageView.getLocationOnScreen(iArr)
//                            this@ImageAlbumFragment.fragmentManager!!.beginTransaction()
//                                .setReorderingAllowed(true)
//                                .addToBackStack(javaClass.name).commitAllowingStateLoss()
//                            this@ImageAlbumFragment.activity!!.overridePendingTransition(0, 0)
//                        }
//                }
//
//                // com.example.iphoto.adapter.PhotoPreviewAlbumAdapter.OnClickCuston
//                fun onCLickChoose(position: Int) {
//                    val it: MutableIterator<GalleryModel?> = mGalleryModels.iterator()
//                    var i = 0
//                    while (it.hasNext()) {
//                        if (it.next()?.choose == true) {
//                            i++
//                        }
//                    }
//                    binding.inMultiSelect.tvCountItemSelect.text =
//                        i.toString() + " " + this@ImageAlbumFragment.getString(R.string.selected_items)
//                }
//
//                // com.example.iphoto.adapter.PhotoPreviewAlbumAdapter.OnClickCuston
//
//            })
//        binding.rcvImage.adapter = this.mAdapter
//        val zoomItemAnimator = ZoomItemAnimator()
//        this.itemAnimator = zoomItemAnimator
//        zoomItemAnimator.setup(binding.rcvImage)
//        binding.rcvImage.scrollToPosition(mGalleryModels.size - 1)
//    }
}