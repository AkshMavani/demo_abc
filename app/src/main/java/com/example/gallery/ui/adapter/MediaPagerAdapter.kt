package com.example.gallery.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.example.demo_full.R
import com.example.gallery.ui.model.GalleryModel
import com.github.chrisbanes.photoview.PhotoView

class MediaPagerAdapter(private val mediaPaths: List<GalleryModel>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(container.context)
        val view = inflater.inflate(R.layout.item_media_page, container, false)

        val mediaPath = mediaPaths[position]
        val imageView: PhotoView = view.findViewById(R.id.imageView)
        val videoView: VideoView = view.findViewById(R.id.videoView)

        // Display image or video based on the mediaPath
        if (mediaPath.path!!.endsWith(".mp4")) {
            videoView.visibility = View.VISIBLE
            imageView.visibility = View.GONE
            videoView.setVideoPath(mediaPath.path)
            videoView.start()
        } else {
            imageView.visibility = View.VISIBLE
            videoView.visibility = View.GONE
            Glide.with(container.context).load(mediaPath.path).into(imageView)
        }

        container.addView(view)
        return view
    }

    override fun getCount(): Int = mediaPaths.size

    override fun isViewFromObject(view: View, obj: Any): Boolean = view == obj

    override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
        container.removeView(obj as View)
    }
}

