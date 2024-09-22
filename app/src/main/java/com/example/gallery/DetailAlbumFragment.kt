package com.example.gallery

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.DecelerateInterpolator
import androidx.activity.OnBackPressedCallback
import androidx.core.view.GestureDetectorCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R
import com.example.demo_full.databinding.FragmentDetailAlbumBinding
import com.example.gallery.ui.MediaRepository
import com.example.gallery.ui.MediaViewModel
import com.example.gallery.ui.MediaViewModelFactory
import com.example.gallery.ui.adapter.ThumbnailAdapter
import com.example.gallery.ui.model.GalleryModel
import com.example.gallery.util.DragLayout
import com.example.gallery.util.IVideoUpdate
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper

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



    private lateinit var detector: GestureDetectorCompat
    var hScale: Float = 0f
    var height: Int = 0

    var isLoad: Boolean = false

    var left: Int = 0
    lateinit var name: String

    var top: Int = 0
    var type: Int = 0
    var wScale: Float = 0f
    var widget: Int = 0
    private var x1: Float = 0f
    private var x2: Float = 0f

    var isplay: Boolean = true
    var ismute: Boolean = true
    var screenLocation1 = IntArray(2)
    private var skipNavigationView: Boolean = false
    private var upKeyEventTime: Long = 0
    private var downKeyEventTime: Long = 0



    private var _binding: FragmentDetailAlbumBinding? = null
    var pagerAdapterMediaFragment: PagerAdapterMediaFragment? = null
    // with the backing property of the kotlin we extract
    // the non null value of the _binding
    private val binding get() = _binding!!
    var mGalleryModels: List<GalleryModel> = ArrayList()
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var thumbnailAdapter: ThumbnailAdapter? = null
    private val mediaViewModel: MediaViewModel by viewModels { MediaViewModelFactory(MediaRepository(requireContext())) }
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
        binding.inHeader.imgBack.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(), R.id.container_gallery)
            navController.navigate(R.id.navigation_home)
        }
        (activity as? MainActivity11)?.navView?.visibility = View.GONE
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
//                // from class: com.example.iphoto.ui.DetailImageFragment.1
//                // androidx.activity.OnBackPressedCallback
//                override fun handleOnBackPressed() {
//                    if (this@DetailAlbumFragment.getFragmentManager() != null) {
//                        this@DetailAlbumFragment.runExitAnimaton()
//                    }
//                }
//            })

        val bundle = arguments
        if (bundle != null) {
            val locationOnScreen = bundle.getIntArray("screenLocation")
             widget = bundle.getInt("widget")
             height = bundle.getInt("height")
             currentPosition = bundle.getInt("position")
            val imagePath = bundle.getString("imagePath")

            // Use the retrieved values as needed
            Log.e("DetailImageFragment", "locationOnScreen: $locationOnScreen")
            Log.e("DetailImageFragment", "imagePath: $imagePath")

        }
//        binding.dragLayout.getViewTreeObserver()
//            .addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
//                // from class: com.example.iphoto.ui.DetailImageFragment.7
//                // android.view.ViewTreeObserver.OnPreDrawListener
//                override fun onPreDraw(): Boolean {
//                    this@DetailAlbumFragment.binding.dragLayout.getViewTreeObserver()
//                        .removeOnPreDrawListener(this)
//                    val iArr = IntArray(2)
//                    this@DetailAlbumFragment.binding.dragLayout.getLocationOnScreen(iArr)
//                    val detailImageFragment: DetailAlbumFragment = this@DetailAlbumFragment
//                    detailImageFragment.left = detailImageFragment.screenLocation1.get(0) - iArr[0]
//                    val detailImageFragment2: DetailAlbumFragment = this@DetailAlbumFragment
//                    detailImageFragment2.top = detailImageFragment2.screenLocation1.get(1) - iArr[1]
//                    this@DetailAlbumFragment.wScale = (1000 / this@DetailAlbumFragment.binding.dragLayout.getWidth()).toFloat()
//                    this@DetailAlbumFragment.hScale = (1000 / this@DetailAlbumFragment.binding.dragLayout.getHeight()).toFloat()
//                    this@DetailAlbumFragment.runEnterAnimation()
//                    return true
//                }
//            })
        binding.rcvThumbImageBottom.setLayoutManager(LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        mediaViewModel.galleryItemsLiveData.observe(requireActivity(), Observer { mediaItems ->
            mGalleryModels=mediaItems
            thumbnailAdapter = ThumbnailAdapter(context, mediaItems, object :
                ThumbnailAdapter.OnClickCustom {
                // from class: com.example.iphoto.ui.DetailImageFragment.5
                // com.example.iphoto.adapter.ThumbnailAdapter.OnClickCustom
                override fun onCLick1(index: Int) {
                   binding.pagerPhotos.post(Runnable
                    { binding.pagerPhotos.setCurrentItem(index, true) })
                }
            })
        })
        val gravitySnapHelper = GravitySnapHelper(17)
        gravitySnapHelper.attachToRecyclerView(binding.rcvThumbImageBottom)
        gravitySnapHelper.setGravity(17, true)
        gravitySnapHelper.setSnapListener { position ->

            // from class: com.example.iphoto.ui.DetailImageFragment.8
            // com.github.rubensousa.gravitysnaphelper.GravitySnapHelper.SnapListener
           currentPosition = position
            this@DetailAlbumFragment.binding.pagerPhotos.post(Runnable
            // from class: com.example.iphoto.ui.DetailImageFragment.8.1
            // java.lang.Runnable
            {

                this@DetailAlbumFragment.binding.pagerPhotos.setCurrentItem(position, false)
            })
        }
        gravitySnapHelper.snapToPadding = true
        binding.rcvThumbImageBottom.scrollToPosition(currentPosition)
        binding.rcvThumbImageBottom.adapter = thumbnailAdapter
        Handler().postDelayed({
           binding.rcvThumbImageBottom.scrollToPosition(
               currentPosition
            )
        }, 100L)
        binding.pagerPhotos.adapter = pagerAdapterMediaFragment

        binding.pagerPhotos.setCurrentItem(currentPosition, false)
        setuplist()
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
        binding.dragLayout.setOnClickListener{

        }
//        binding.dragLayout.setDragListener(object : DragLayout.DragListener {
//            // from class: com.example.iphoto.ui.DetailImageFragment.12
//            // com.example.iphoto.view.DragLayout.DragListener
//            override fun onDragStarted(check: Boolean) {}
//
//            // com.example.iphoto.view.DragLayout.DragListener
//            override fun onDragFinished(x: Float, y: Float) {
//                if (y > 50.0f || y < -100.0f) {
//                    this@DetailAlbumFragment.runExitAnimaton()
//                    return
//                }
//                this@DetailAlbumFragment.binding.inBottom.getRoot().setVisibility(View.VISIBLE)
//                this@DetailAlbumFragment.binding.navigationViewDetail.setVisibility(View.VISIBLE)
//                this@DetailAlbumFragment.binding.inHeader.getRoot().setVisibility(View.VISIBLE)
//                this@DetailAlbumFragment.binding.rcvThumbImageBottom.setVisibility(View.VISIBLE)
//                val typedValue = TypedValue()
//                this@DetailAlbumFragment.getContext()?.getTheme()
//                    ?.resolveAttribute(R.attr.background_color_main, typedValue, true)
//                if (typedValue.resourceId != 0) {
//                    this@DetailAlbumFragment.binding.dragLayout.setBackgroundResource(typedValue.resourceId)
//                } else {
//                    this@DetailAlbumFragment.binding.dragLayout.setBackgroundColor(typedValue.data)
//                }
//            }
//
//            // com.example.iphoto.view.DragLayout.DragListener
//            override fun onDrag(x: Float, y: Float) {
//                this@DetailAlbumFragment.binding.inHeader.getRoot().setVisibility(View.INVISIBLE)
//                this@DetailAlbumFragment.binding.inBottom.getRoot().setVisibility(View.INVISIBLE)
//                this@DetailAlbumFragment.binding.navigationViewDetail.setVisibility(View.INVISIBLE)
//                this@DetailAlbumFragment.binding.rcvThumbImageBottom.setVisibility(View.INVISIBLE)
//                this@DetailAlbumFragment.binding.dragLayout.setBackgroundColor(0)
//                this@DetailAlbumFragment.binding.pagerPhotos.setBackgroundColor(0)
//                this@DetailAlbumFragment.binding.rrrrr.setBackgroundColor(0)
//            }
//        })


        return binding.root
    }

    companion object {
        const val MIN_DISTANCE = 150
        const val VOLUME_PRESSED_INTERVAL = 100
        var currentPosition=0
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

    fun setuplist() {
        if (mGalleryModels.size > 0) {
            pagerAdapterMediaFragment =
                PagerAdapterMediaFragment(this, mGalleryModels, object : IVideoUpdate {
                    // from class: com.example.iphoto.ui.DetailImageFragment.15
                    // com.example.iphoto.callback.IVideoUpdate
                    override fun resumePlay() {
                      //  setPlayVideo(true)
                    }

                    // com.example.iphoto.callback.IVideoUpdate
                    override fun updateClickItem() {
                        if (this@DetailAlbumFragment.binding.inHeader.getRoot()
                                .getVisibility() === View.VISIBLE
                        ) {
                            this@DetailAlbumFragment.binding.inHeader.getRoot().setVisibility(View.INVISIBLE)
                            this@DetailAlbumFragment.binding.inBottom.getRoot().setVisibility(View.INVISIBLE)
                            this@DetailAlbumFragment.binding.navigationViewDetail.setVisibility(View.INVISIBLE)
                            this@DetailAlbumFragment.binding.inBottomDelete.getRoot().setVisibility(
                                View.GONE)
                            this@DetailAlbumFragment.binding.rcvThumbImageBottom.setVisibility(View.INVISIBLE)

                            return
                        }
                        this@DetailAlbumFragment.binding.inHeader.getRoot().setVisibility(View.VISIBLE)
                        this@DetailAlbumFragment.binding.inBottom.getRoot().setVisibility(View.VISIBLE)
                        this@DetailAlbumFragment.binding.navigationViewDetail.setVisibility(View.VISIBLE)
                        this@DetailAlbumFragment.binding.inBottomDelete.getRoot().setVisibility(
                            View.GONE)
                        this@DetailAlbumFragment.binding.rcvThumbImageBottom.setVisibility(View.VISIBLE)
                    }
                })
            binding.pagerPhotos.adapter = pagerAdapterMediaFragment
            binding.pagerPhotos.setCurrentItem(currentPosition, false)
        }
        val pagerAdapterMediaFragment = pagerAdapterMediaFragment
        pagerAdapterMediaFragment?.notifyDataSetChanged()
    }


    @SuppressLint("ObjectAnimatorBinding")
    fun runExitAnimaton() {
        binding.inHeader.root.visibility = View.INVISIBLE
        binding.inBottom.root.visibility = View.INVISIBLE
        binding.navigationViewDetail.visibility = View.INVISIBLE
        binding.rcvThumbImageBottom.visibility = View.INVISIBLE
        binding.dragLayout.setBackgroundColor(0)
        binding.pagerPhotos.setBackgroundColor(0)
        val j: Long = 150
        binding.dragLayout.animate().setDuration(j).scaleX(wScale).scaleY(hScale).translationX(
            left.toFloat()
        ).translationY(top.toFloat()).withEndAction { this@DetailAlbumFragment.m224x291f216a() }
        val ofInt = ObjectAnimator.ofInt(binding.root, "alpha", 0, 255)
        ofInt.duration = j
        ofInt.start()
    }
     @SuppressLint("UseRequireInsteadOfGet")
     fun m224x291f216a() {
        (activity as MainActivity11).hideDelete()
         (activity as MainActivity11).navView?.setVisibility(View.VISIBLE)
        Handler().post {
            if (this@DetailAlbumFragment.getFragmentManager()!!.getBackStackEntryCount() >= 0) {
                this@DetailAlbumFragment.getFragmentManager()!!.popBackStack()
            } else {
                this@DetailAlbumFragment.getActivity()!!.onBackPressed()
            }
        }
    }
    fun runEnterAnimation() {
        binding.dragLayout.pivotX = 0.0f
        binding.dragLayout.pivotY = 0.0f
        try {
            binding.dragLayout.scaleX = wScale
            binding.dragLayout.scaleY = hScale
        } catch (unused: Exception) {
        }
        binding.dragLayout.translationX = left.toFloat()
        binding.dragLayout.translationY = top.toFloat()
        val j: Long = 150
        binding.dragLayout.animate().setDuration(j).scaleX(1.0f).scaleY(1.0f).translationX(0.0f)
            .translationY(0.0f).interpolator =
            DecelerateInterpolator()
        val ofInt = ObjectAnimator.ofInt(binding.root, "alpha", 0, 255)
        ofInt.duration = j
        ofInt.start()
    }

}