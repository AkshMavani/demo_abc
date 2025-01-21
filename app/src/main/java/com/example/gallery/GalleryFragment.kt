package com.example.gallery

import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.ContentUris
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo_full.R
import com.example.demo_full.databinding.FragmentGallery2Binding
import com.example.gallery.ui.MediaAdapter
import com.example.gallery.ui.MediaRepository
import com.example.gallery.ui.MediaViewModel
import com.example.gallery.ui.MediaViewModelFactory
import com.example.gallery.ui.adapter.DayAdapter
import com.example.gallery.ui.adapter.MonthAdapter
import com.example.gallery.ui.adapter.YearAdapter
import com.example.gallery.ui.click_item
import com.example.gallery.ui.model.GalleryModel
import com.example.gallery.util.StickyHeaderGridLayoutManager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import java.io.File
import java.util.Calendar


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [GalleryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GalleryFragment : Fragment() {
    var MODE_EDIT = 0

    var MODE_SELECT = 1

    lateinit var deleteLauncher: ActivityResultLauncher<Intent>
    lateinit var _binding: FragmentGallery2Binding
    lateinit var stickyHeaderGridLayoutManager: StickyHeaderGridLayoutManager
    var defaultMode: Int = MODE_SELECT
    lateinit var mAdapter: MediaAdapter
    lateinit var dayAdapter:DayAdapter
    var mGalleryModels: ArrayList<GalleryModel?> = ArrayList()


    //var mGalleryYearModels: ArrayList<GalleryModel> = ArrayList()
     var arr:ArrayList<GalleryModel> = ArrayList()
    var newArratList:ArrayList<String> = ArrayList()
    var newArratListIndex:ArrayList<Int> = ArrayList()
    var mGalleryDayModels: List<List<GalleryModel>?> = java.util.ArrayList()
    var mGalleryMonthModels: List<List<GalleryModel>?> = java.util.ArrayList()
    var mGalleryYearModels: List<GalleryModel?> = java.util.ArrayList()
    private val mediaViewModel: MediaViewModel by viewModels { MediaViewModelFactory(MediaRepository(requireContext())) }
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

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentGallery2Binding.inflate(inflater, container, false)


        deleteLauncher = registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                Log.e("TAG", "onCreateView:deelte success ")
                val data=mAdapter.selectItems()
                Log.e("DATA123", ":>>>>>>####$data ")
//                Log.e("DATA123", ":>>>>>>####$selectedData ", )
//                for (i in selectedData){
//                    Log.e("DATA123", "onCreateView:>>>>>>####$data ", )
//                    Log.e("DATA123", "onCreateView:>>>>>>####$i ", )
//                    data.removeAt(i)
//                    Log.e("DATA123", "onCreateView:>>>>>>####$data ", )
//                    mAdapter.notifyDataSetChanged()
//
//                }

                // Handle successful deletion
                // Optionally, show a toast or update your UI
            }
        }

        val layoutManager = GridLayoutManager(requireContext(), 3)
        binding.rcvMedia.layoutManager = layoutManager
//        mediaViewModel.mediaItemsLiveData.observe(requireActivity(), Observer { mediaItems ->
//            Log.e("MediaItems", "onCreateView:>>>>>$mediaItems ")
//          val mediaAdapter =  MediaAdapter(requireContext(), mediaItems)
//            binding.rcvMedia.adapter=mediaAdapter
//        })


        binding.tabMode.addTab(binding.tabMode.newTab().setText(getText(R.string.year)))
        binding.tabMode.addTab(binding.tabMode.newTab().setText(getText(R.string.month)))
        binding.tabMode.addTab(binding.tabMode.newTab().setText(getText(R.string.day)))
        binding.tabMode.addTab(binding.tabMode.newTab().setText(getText(R.string.all_media)))
        binding.tabMode.selectTab(binding.tabMode.getTabAt(3))
        binding.tabMode.isSmoothScrollingEnabled = true
        mediaViewModel.galleryItemsLiveData.observe(requireActivity(), Observer { mediaItems ->
            Log.e("Gallery", "onCreateView:>>>>>$mediaItems ")
            mGalleryModels=mediaItems as ArrayList<GalleryModel?>
            year(mediaItems)
            month(mediaItems)
            loadDataDay(mediaItems)
            getAllImages(mediaItems)
        })
        binding.btnGalleryMore.setOnClickListener { popupWindow() }
        binding.btnSelect.setOnClickListener {
            if (defaultMode == MODE_SELECT) {
                defaultMode = MODE_EDIT
                (activity as? MainActivity11)?.displayDelete()
                (activity as? MainActivity11)?.navView?.visibility = View.GONE
                (activity as? MainActivity11)?.view?.findViewById<ImageView>(R.id.img_share)!!.setOnClickListener {
                    Log.e("TAG_SELECted", "click: ")
                   val data= mAdapter.getSelectedImagePaths()
                    shareMultipleImages(data)
                }
                (activity as? MainActivity11)?.view?.findViewById<ImageView>(R.id.img_delete)!!.setOnClickListener {
                    val data= mAdapter.getSelectedImagePaths()
                    val share= mAdapter.selectItems()
                    Log.e("SHRT12", "onCreateView:>>$share ", )
                    Log.e("SHRT12", "onCreateView:>>$data ", )
//                    share=newArratListIndex
//                    data=newArratList
                    deleteMultipleMedia(data)




                }
                binding.cardview.visibility = View.INVISIBLE
                binding.btnSelect.text = getString(R.string.cancel)

                if (binding.tabMode.selectedTabPosition != 2) {
                    if (binding.tabMode.selectedTabPosition == 3 && mAdapter != null) {
                       // mAdapter?.setChoose(true)
                        mAdapter.toggleSelectMode()
                        mAdapter?.notifyDataSetChanged()
                    }
                } else {
                    dayAdapter?.setChoose(true)
                    dayAdapter?.notifyDataSetChanged()
                }
            } else if (defaultMode == MODE_EDIT) {
                defaultMode = MODE_SELECT
                (activity as? MainActivity11)?.hideDelete()
                (activity as? MainActivity11)?.navView?.visibility = View.VISIBLE
                binding.cardview.visibility = View.VISIBLE
                binding.btnSelect.text = getString(R.string.select_text)

                if (mAdapter != null) {
                    if (binding.tabMode.selectedTabPosition != 2) {
                        if (binding.tabMode.selectedTabPosition == 3) {
                            mAdapter.toggleSelectMode()
                            //FIleUtils.formatListgallery(mGalleryModels)
                            mAdapter?.notifyDataSetChanged()
                        }
                    } else {
                        dayAdapter?.setChoose(true)
                        dayAdapter?.notifyDataSetChanged()
                    }
                }
            }

        }


        binding.tabMode.setOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab!!.position==0){

                    binding.rcvYear.visibility=View.VISIBLE
                    binding.rcvMonth.visibility=View.GONE
                    binding.rcvDay.visibility=View.GONE
                    binding.rcvMedia.visibility=View.GONE
                } else if (tab.position==1){

                    binding.rcvYear.visibility=View.GONE
                    binding.rcvMonth.visibility=View.VISIBLE
                    binding.rcvDay.visibility=View.GONE
                    binding.rcvMedia.visibility=View.GONE
                } else if (tab.position==2){

                    binding.rcvYear.visibility=View.GONE
                    binding.rcvMonth.visibility=View.GONE
                    binding.rcvDay.visibility=View.VISIBLE
                    binding.rcvMedia.visibility=View.GONE
                } else if (tab.position==3){
                    binding.rcvYear.visibility=View.GONE
                    binding.rcvMonth.visibility=View.GONE
                    binding.rcvDay.visibility=View.GONE
                    binding.rcvMedia.visibility=View.VISIBLE
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
        })


        return binding.root
    }

    companion object {
        var currentPosition = 0
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment GalleryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            GalleryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }



    fun getListImageByDay(galleryModels: List<GalleryModel>?): List<List<GalleryModel>> {
        val result = mutableListOf<List<GalleryModel>>()  // The final list of grouped images by day
        var dayList = mutableListOf<GalleryModel>()  // Temporary list for images of the same day

        if (!galleryModels.isNullOrEmpty()) {
            var currentDay = galleryModels[0].days  // Start with the first item's day

            for (model in galleryModels) {
                if (model.path != null) {
                    if (model.days == currentDay) {
                        // If the current item is from the same day, add it to the current list
                        dayList.add(model)
                    } else {
                        // If we encounter a new day, add the current list to the result and start a new list
                        result.add(dayList.reversed())  // Add current day's list to result (reversed to maintain correct order)
                        dayList = mutableListOf()  // Reset the day list
                        currentDay = model.days  // Update the current day
                        dayList.add(model)  // Add the new day's first item
                    }
                }
            }

            // Add the last group to the result after the loop
            if (dayList.isNotEmpty()) {
                result.add(dayList.reversed())
            }
        }

        return result
    }

    //    fun getListImageByMonth(galleryModels: List<GalleryModel>?): List<List<GalleryModel>> {
//        val result = mutableListOf<List<GalleryModel>>()
//        val monthList = mutableListOf<GalleryModel>()
//        result.clear()
//        monthList.clear()
//        if (!galleryModels.isNullOrEmpty()) {
//            var currentDay = galleryModels[0].days
//            var currentMonth = galleryModels[0].month
//            var currentYear = galleryModels[0].year
//
//            monthList.add(galleryModels[0])
//            for (model in galleryModels) {
//                if (model.path != null) {
//                    if (model.days != currentDay) {
//                        currentDay = model.days
//                        monthList.add(galleryModels[galleryModels.indexOf(model) - 1])
//                    }
//                    if (model.month != currentMonth || model.year != currentYear) {
//                        currentMonth = model.month
//                        currentYear = model.year
//                        result.add(monthList)
//                        monthList.clear()
//                    }
//                }
//            }
//            monthList.add(galleryModels.last())
//            result.add(monthList)
//        }
//        return result
//    }
        fun getListImageByMonth(galleryModels: List<GalleryModel>?): List<List<GalleryModel>> {
            val arrayList = ArrayList<List<GalleryModel>>()
            var arrayList2 = ArrayList<GalleryModel>()

            if (galleryModels != null && galleryModels.isNotEmpty()) {
                var days = galleryModels[0].days
                var month = galleryModels[0].month
                var year = galleryModels[0].year
                arrayList2.add(galleryModels[0])

                for (i in galleryModels.indices) {
                    if (galleryModels[i].path != null) {
                        if (galleryModels[i].days != days) {
                            days = galleryModels[i].days
                            arrayList2.add(galleryModels[i - 1])
                        }
                        if (galleryModels[i].month != month || galleryModels[i].year != year) {
                            month = galleryModels[i].month
                            year = galleryModels[i].year
                            arrayList.add(arrayList2)
                            arrayList2 = ArrayList()
                        }
                    }
                }
                arrayList2.add(galleryModels[galleryModels.size - 1])
                arrayList.add(arrayList2)
            }

            return arrayList
        }



    fun getListImageByYear(galleryModels: List<GalleryModel>?): List<GalleryModel> {
        val result = mutableListOf<GalleryModel>()

        if (!galleryModels.isNullOrEmpty()) {
            var currentYear = galleryModels[0].year

            for (model in galleryModels) {
                if (model.path != null) {
                    if (model.year != currentYear) {
                        currentYear = model.year
                        result.add(galleryModels[galleryModels.indexOf(model) - 1])
                    }
                    if (galleryModels.indexOf(model) == galleryModels.size - 1) {
                        result.add(model)
                    }
                }
            }
        }
        return result
    }


    fun month(mediaItems:List<GalleryModel>){

        val month=getListImageByMonth(mediaItems)
        Log.e("MPNTH1", "month: >>$month")
        Log.e("MPNTH1", "month: >>$mediaItems")
        this.mGalleryMonthModels = month

        if (month.size > 0) {
            val monthAdapter = MonthAdapter(context, month, object : MonthAdapter.OnClickCustom {
                override fun onCLick1(galleryModel: GalleryModel?) {
//                        this@GalleryFragment.m251xcc14c062(galleryModel)
                    selectmonth(galleryModel!!)
                }
            })
            binding.rcvMonth.adapter = monthAdapter
            val stickyHeaderGridLayoutManager = StickyHeaderGridLayoutManager(1)
            stickyHeaderGridLayoutManager.setHeaderBottomOverlapMargin(
                resources.getDimensionPixelSize(
                    R.dimen._10sdp
                )
            )
            binding.rcvMonth.layoutManager = stickyHeaderGridLayoutManager
            stickyHeaderGridLayoutManager.scrollToPosition(monthAdapter.getItemCount() - 1)
        }
    }


    fun year(mediaItems: List<GalleryModel>) {
        // Group images by year
        val groupedByYear = mediaItems.groupBy { galleryModel ->
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = galleryModel.datetaken
            calendar.get(Calendar.YEAR) // Extract the year
        }

        // Create a list that will contain only the first image of each year
        val yearWiseGalleryModels = ArrayList<GalleryModel>()

        groupedByYear.forEach { (year, imagesInYear) ->
            // Get the first image of the year
            val firstImageOfYear = imagesInYear.firstOrNull()
            if (firstImageOfYear != null) {
                yearWiseGalleryModels.add(firstImageOfYear)
            }
        }

        mGalleryYearModels = yearWiseGalleryModels

        // Set up the RecyclerView with the updated adapter
        if (yearWiseGalleryModels.isNotEmpty()) {
            binding.rcvYear.adapter = YearAdapter(activity, yearWiseGalleryModels, object : YearAdapter.OnClickCuston {
                override fun onCLick1(galleryModel: GalleryModel) {
                    binding.tabMode.selectTab(binding.tabMode.getTabAt(1))
                    scrollToMonth(galleryModel)
                }
            })

            val linearLayoutManager = LinearLayoutManager(context)
            binding.rcvYear.layoutManager = linearLayoutManager
        }
    }

    // Function to scroll to the specific month (same as before)
    private fun scrollToMonth(galleryModel: GalleryModel) {
        var i = 0
        for (i2 in mGalleryMonthModels.indices) {
            i += mGalleryMonthModels[i2]?.size ?: 0
            for (i3 in mGalleryMonthModels[i2]?.indices ?: 0 until 0) {
                if (galleryModel.datetaken == mGalleryMonthModels[i2]?.get(i3)?.datetaken) {
                    binding.rcvMonth.layoutManager?.scrollToPosition(i + i2)
                    return
                }
            }
        }
    }

//    fun loadDataDay(mediaItems: List<GalleryModel>) {
//        if (binding.rcvDay.adapter != null) {
//            return
//        }
//
//        // Get the list of images grouped by day
//        val listImageByDay: List<List<GalleryModel>> = getListImageByDay(mediaItems)
//        Log.e("Img_Day", "loadDataDay:>>>>>>>>>>$listImageByDay ", )
//        if (listImageByDay.isEmpty()) {
//            return
//        }
//
//        dayAdapter = DayAdapter(
//            mediaViewModel,
//            context,
//            listImageByDay,
//            object : DayAdapter.OnClickCustom {
//
//
//                override fun onCLick1(view: View?, galleryModel: GalleryModel?) {
//                    m250x4bc8bbfd(view!!, galleryModel!!)
//                }
//            }
//        )
//
//
//        val stickyHeaderGridLayoutManager = StickyHeaderGridLayoutManager(3)
//        stickyHeaderGridLayoutManager.spanSizeLookup = object : StickyHeaderGridLayoutManager.SpanSizeLookup() {
//            override fun getSpanSize(section: Int, position: Int): Int {
//                Log.e("POSITION12", "loadDataDay: >>>###$position", )
//                return if (position == 1 || position == 2 || position == 3) 1 else 3
//            }
//        }
//
//        stickyHeaderGridLayoutManager.setHeaderBottomOverlapMargin(
//            resources.getDimensionPixelSize(R.dimen._10sdp)
//        )
//
//        binding.rcvDay.layoutManager = stickyHeaderGridLayoutManager
//        binding.rcvDay.adapter = dayAdapter
//
//        stickyHeaderGridLayoutManager.scrollToPosition(dayAdapter.getItemCount() - 1)
//    }


    fun loadDataDay(mediaItems: List<GalleryModel>) {
        if (binding.rcvDay.adapter != null) {
            return
        }
        val listImageByDay: List<List<GalleryModel>?> = getListImageByDay(mediaItems)
        mGalleryDayModels = listImageByDay
        if (listImageByDay.size == 0) {
            return
        }

        stickyHeaderGridLayoutManager = StickyHeaderGridLayoutManager(3)
        stickyHeaderGridLayoutManager.setSpanSizeLookup(object : StickyHeaderGridLayoutManager.SpanSizeLookup() {

            override fun getSpanSize(section: Int, position: Int): Int {
                Log.e("POS12", "getSpanSize:>>>###$position ")
                return if (position == 1 || position == 2 || position == 3) 1 else 3
            }
        })

        stickyHeaderGridLayoutManager.setHeaderBottomOverlapMargin(
            resources.getDimensionPixelSize(
                R.dimen._10sdp
            )
        )

        binding.rcvDay.layoutManager = stickyHeaderGridLayoutManager
        dayAdapter = DayAdapter(mediaViewModel, context, mGalleryDayModels
        ) { view, galleryModel ->

            // from class: com.example.iphoto.ui.gallery.GalleryFragment$$ExternalSyntheticLambda4
            // com.example.iphoto.adapter.DayAdapter.OnClickCustom
            m250x4bc8bbfd(view, galleryModel!!)
        }
        binding.rcvDay.adapter = dayAdapter
        stickyHeaderGridLayoutManager.scrollToPosition(dayAdapter.itemCount - 1)




    }

    fun getAllImages(mediaItems: List<GalleryModel>){
         mAdapter =  MediaAdapter(requireContext(), mediaItems,object :click_item{
             override fun click(galleryModel: GalleryModel, position: Int, view: View) {
                 m250x4bc8bbfd(view,galleryModel)
             }

         })
            binding.rcvMedia.adapter=mAdapter
    }
    fun selectmonth(galleryModel: GalleryModel) {
        binding.tabMode.selectTab(binding.tabMode.getTabAt(2))

        var i = 0
        for (i2 in mGalleryDayModels.indices) {
            i += if (mGalleryDayModels[i2]!!.size >= 4) 4 else 1

            for (i3 in mGalleryDayModels[i2]!!.indices) {
                if (galleryModel.datetaken == mGalleryDayModels[i2]!![i3].datetaken) {
                    mGalleryDayModels[i2]!![i3]
                    binding.rcvDay.layoutManager?.scrollToPosition((i + i2) - 1)
                }
            }
        }
    }

    fun shareMultipleImages(imageUrls: List<String?>) {
        val imageUris = ArrayList<Uri>()
        for (imageUrl in imageUrls) {
            val imageFile = File(imageUrl)
            var imageUri: Uri
            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                FileProvider.getUriForFile(requireContext(), requireContext().applicationContext.packageName + ".provider", imageFile)
            } else {
                Uri.fromFile(imageFile)
            }
            imageUris.add(imageUri)
        }

        // Create an Intent to share multiple images
        val shareIntent = Intent(Intent.ACTION_SEND_MULTIPLE)
        shareIntent.type = "image/*"
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris)
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        requireContext().startActivity(Intent.createChooser(shareIntent, "Share Images"))
    }


//    fun deleteMultipleImages(imageUrls: List<String?>) {
//        val urisToDelete: MutableList<Uri> = ArrayList()
//        for (imageUrl in imageUrls) {
//            val imageFile = File(imageUrl)
//            var imageUri: Uri?
//            imageUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                // For Android 7.0 and above, use FileProvider
//                FileProvider.getUriForFile(
//                    requireContext(),    requireContext().getApplicationContext().getPackageName() + ".provider",
//                    imageFile
//                )
//            } else {
//                // For Android below 7.0, directly use the Uri from the file path
//                Uri.fromFile(imageFile)
//            }
//
//            // Find the content Uri for the image
//            val contentUri = getContentUri(imageFile)
//            if (contentUri != null) {
//                urisToDelete.add(contentUri)
//            }
//        }
//
////        // Create delete request for MediaStore
////        if (!urisToDelete.isEmpty()) {
////
////            val deleteIntent:Intent = MediaStore.createDeleteRequest(requireContext().contentResolver, urisToDelete)
////            deleteLauncher.launch(deleteIntent)
////        }
//
//        if (urisToDelete.isNotEmpty()) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                // For Android 11 and above
//                val deletePendingIntent: PendingIntent =
//                    MediaStore.createDeleteRequest(requireContext().contentResolver, urisToDelete)
//
//                try {
//                    startIntentSenderForResult(
//                        deletePendingIntent.intentSender,
//                        101, // Request code for identifying this action
//                        null,
//                        0,
//                        0,
//                        0,
//                        null
//                    )
//                } catch (e: IntentSender.SendIntentException) {
//                    e.printStackTrace()
//                }
//            }
//        }
//    }
//
//    // Helper function to get the MediaStore content Uri
//    private fun getContentUri(imageFile: File): Uri? {
//        val filePath = imageFile.absolutePath
//        var contentUri: Uri? = null
//        val projection = arrayOf(MediaStore.Images.Media._ID)
//        val selection = MediaStore.Images.Media.DATA + "=?"
//        val selectionArgs = arrayOf(filePath)
//       context?.getContentResolver()?.query(
//           MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//           projection, selection, selectionArgs, null
//       ).use { cursor ->
//            if (cursor != null && cursor.moveToFirst()) {
//                val id: Int =
//                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
//                contentUri = ContentUris.withAppendedId(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    id.toLong()
//                )
//            }
//        }
//        return contentUri
//    }


    fun deleteMultipleMedia(mediaUrls: List<String?>) {
        var mediaUri: Uri
        val urisToDelete: MutableList<Uri> = ArrayList()
        for (mediaUrl in mediaUrls) {
            val mediaFile = File(mediaUrl)

            mediaUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // For Android 7.0 and above, use FileProvider
                FileProvider.getUriForFile(
                    requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider",
                    mediaFile
                )
            } else {
                // For Android below 7.0, directly use the Uri from the file path
                Uri.fromFile(mediaFile)
            }

            // Find the content Uri for the media
            val contentUri = getContentUri(mediaFile)
            if (contentUri != null) {
                urisToDelete.add(contentUri)
            }
        }

        if (urisToDelete.isNotEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                // For Android 11 and above
                val deletePendingIntent: PendingIntent =
                    MediaStore.createDeleteRequest(requireContext().contentResolver, urisToDelete)

                try {
                    startIntentSenderForResult(
                        deletePendingIntent.intentSender,
                        101, // Request code for identifying this action
                        null,
                        0,
                        0,
                        0,
                        null
                    )
                } catch (e: IntentSender.SendIntentException) {
                    e.printStackTrace()
                }
            } else {
                // For Android below 11
                for (uri in urisToDelete) {
                    requireContext().contentResolver.delete(uri, null, null)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.e("TAG111", "onActivityResult:$resultCode ", )
        Log.e("TAG111", "onActivityResult:$requestCode ", )
        if (requestCode==101 && resultCode== -1){
            val data1=mAdapter.selectItems()
            Log.e("DATA123", ":>>>>>>####${data1.size} ")
            Log.e("DATA123", ":>>>>>>####${data1} ")
            for (i in data1){
                Log.e("DATA123", ":>>>>>>####${i} ")
                mGalleryModels.removeAt(i)

                getAllImages(mGalleryModels as ArrayList<GalleryModel>)
            }

        }
        defaultMode = MODE_SELECT
        mAdapter.toggleSelectMode()
    }

    // Helper function to get the MediaStore content Uri
    private fun getContentUri(mediaFile: File): Uri? {
        val filePath = mediaFile.absolutePath
        var contentUri: Uri? = null
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Video.Media._ID
        )
        val selection = "${MediaStore.Images.Media.DATA}=? OR ${MediaStore.Video.Media.DATA}=?"
        val selectionArgs = arrayOf(filePath, filePath)
        context?.getContentResolver()?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, selection, selectionArgs, null
        ).use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val id: Int =
                    cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))
                contentUri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id.toLong()
                )
            } else {
                context?.getContentResolver()?.query(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    projection, selection, selectionArgs, null
                ).use { cursor ->
                    if (cursor != null && cursor.moveToFirst()) {
                        val id: Int =
                            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID))
                        contentUri = ContentUris.withAppendedId(
                            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                            id.toLong()
                        )
                    }
                }
            }
        }
        return contentUri
    }


//    fun m250x4bc8bbfd(view: View, galleryModel: GalleryModel, position: Int) {
//        var selectedPosition = position
//        Log.e("Selectedpos", "m250x4bc8bbfd: ")
//
//        mediaViewModel.galleryItemsLiveData.observe(requireActivity(), Observer { mediaItems ->
//            for (i in mediaItems.indices) {
//                if (mediaItems[i].path == galleryModel.path) {
//                    selectedPosition = i
//                }
//            }
//
//            val imageView = view as ImageView
//            val locationOnScreen = IntArray(2)
//
//            imageView.getLocationOnScreen(locationOnScreen)
//            val myFragment: Fragment = DetailImageFragment()
//
//            fragmentManager?.beginTransaction()
//                ?.setReorderingAllowed(true)
//                ?.replace(
//                    R.id.container_gallery,
//                    DetailImageFragment.newInstances(
//                        locationOnScreen,
//                        imageView.width,
//                        imageView.height
//                    )!!
//                )
//                ?.addToBackStack(null) // Optional: for back navigation
//                ?.commit()
//
//
//
//
//
//
//
//            activity?.overridePendingTransition(0, 0)
//        })
//
//        // baseAdsPopupActivity.showPopupAdsCreateView(activity?.findViewById(R.id.home_main))
//    }

//    fun m250x4bc8bbfd(view: View, galleryModel: GalleryModel, position: Int) {
//        var selectedPosition = position
//        Log.e("Selectedpos", "m250x4bc8bbfd: ")
//
//        mediaViewModel.galleryItemsLiveData.observe(requireActivity(), Observer { mediaItems ->
//            for (i in mediaItems.indices) {
//                if (mediaItems[i].path == galleryModel.path) {
//                    selectedPosition = i
//                }
//            }
//
//            val imageView = view as ImageView
//            val locationOnScreen = IntArray(2)
//            imageView.getLocationOnScreen(locationOnScreen)
//
////            val fragmentManager = requireActivity().supportFragmentManager
////            val currentFragment = fragmentManager.findFragmentById(R.id.container_gallery)
////
////            // Manually remove the current fragment before replacing it
////            currentFragment?.let {
////              //  fragmentManager.beginTransaction().remove(it).commit()
////                fragmentManager.popBackStack()
////            }
////
////            fragmentManager.beginTransaction()
////
////                .replace(
////                    R.id.container_gallery,
////                    DetailImageFragment.newInstances(
////                        locationOnScreen,
////                        imageView.width,
////                        imageView.height
////                    )!!
////                )
////                .addToBackStack(null) // Add to back stack for navigation
////                .commit()
////
////            activity?.overridePendingTransition(0, 0)
////            fragmentManager.popBackStack()
//            val navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment)
//            navController.navigate(R.id.navigation_detail_iamge)
//
//
//        })
//
//    }


    /* synthetic */   @SuppressLint("UseRequireInsteadOfGet")
        fun m250x4bc8bbfd(view: View, galleryModel: GalleryModel) {
            /*
                    for (i in mGalleryModels.indices) {
                        if (mGalleryModels[i]!!.path == galleryModel.path) {
                           currentPosition = i
                        }
                    }

                    // Pass necessary arguments to the DetailImageFragment
                    val iArr = IntArray(2)
                    view.getLocationOnScreen(iArr)
                    val bundle = Bundle()
                    bundle.putIntArray("image_position", iArr)
                    bundle.putInt("image_width", view.width)
                    bundle.putInt("image_height", view.height)
                    bundle.putString("image_path", galleryModel.path)

                    // Use Navigation Component
                    val navController = NavHostFragment.findNavController(this@GalleryFragment)
                    navController.navigate(
                        R.id.action_navigation_home_to_navigation_detail_iamge,
                        bundle
                    )
    */
            for (i in mGalleryModels.indices) {
                if (mGalleryModels[i]?.path.equals(galleryModel.path)) {
                   currentPosition = i
                }
            }
//            val imageView = view as ImageView

            val iArr = IntArray(2)
//            imageView.getLocationOnScreen(iArr)
            this@GalleryFragment.fragmentManager!!.beginTransaction().setReorderingAllowed(true).add(
                R.id.container_gallery,
                DetailImageFragment2.newInstances(iArr, view.width, view.height)
            ).addToBackStack(null).commitAllowingStateLoss()
            this@GalleryFragment.activity!!.overridePendingTransition(0, 0)
    //        val navController = NavHostFragment.findNavController(this@GalleryFragment)
    //        navController.navigate(
    //            R.id.action_navigation_home_to_navigation_detail_iamge)
        }


    fun popupWindow() {
        val popupMenu = PopupMenu(requireContext()  , binding.btnGalleryMore)
        popupMenu.menuInflater.inflate(R.menu.main, popupMenu.menu)

        if (defaultMode == MODE_SELECT) {
            popupMenu.menu.getItem(2).isVisible = false
        } else {
            popupMenu.menu.getItem(2).isVisible = true
        }


        popupMenu.show()
    }




}
