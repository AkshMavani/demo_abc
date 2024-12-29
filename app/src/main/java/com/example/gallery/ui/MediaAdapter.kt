package com.example.gallery.ui


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.demo_full.R
import com.example.gallery.ActivityImageDetail
import com.example.gallery.ui.model.GalleryModel


class MediaAdapter(
    private val context: Context,
    private val mediaItems: List<GalleryModel>
) : RecyclerView.Adapter<MediaAdapter.MediaViewHolder>() {

    // Keep track of selected items
    private val selectedItems = mutableSetOf<Int>()
    private val selectItemIndex:ArrayList<Int> = ArrayList()
    private val selected_Item_Send:ArrayList<String> = ArrayList()

    var isSelectMode: Boolean = false // This flag tracks whether selection mode is active

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.media_item, parent, false)
        return MediaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MediaViewHolder, position: Int) {
        val path = mediaItems[position]
        Log.e("TAG_PATH", "onBindViewHolder: >>>>>>${path.typeMedia}")
        Log.e("TAG_PATH", "onBindViewHolder: >>>>>>${path.path}")

        Glide.with(context).load(path.path).into(holder.mediaImageView)
        if (mediaItems.get(position).path?.contains(".mp4") == true){
            holder.video.visibility=View.VISIBLE
        }else{
        holder.video.visibility=View.GONE
        }
        // Show/Hide the circleView and tickMark based on selection mode and selection state
        if (isSelectMode) {
            holder.circleView.visibility = View.VISIBLE
            holder.tickMark.visibility = if (selectedItems.contains(position)) View.VISIBLE else View.GONE
        } else {
            holder.circleView.visibility = View.GONE
            holder.tickMark.visibility = View.GONE
        }

        // Handle click on image to toggle selection
        holder.itemView.setOnClickListener {
            if (isSelectMode) {
                if (selectedItems.contains(position)) {
                    selectedItems.remove(position)
                } else {
                    selectedItems.add(position)
                }
                notifyItemChanged(position)


            } else {
                // Handle normal image click here (if needed)
//                val intent= Intent(context, ActivityImageDetail::class.java)
//                intent.putExtra("image",mediaItems.get(position).path)
//                intent.putExtra("position",position)
//               context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return mediaItems.size
    }

    class MediaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mediaImageView: ImageView = itemView.findViewById(R.id.mediaImageView)
        val circleView: ImageView = itemView.findViewById(R.id.circleView)
        val tickMark: ImageView = itemView.findViewById(R.id.tickMark)
        val video:ImageView=itemView.findViewById(R.id.img_video)
    }

    // Function to toggle select mode
    fun toggleSelectMode() {
        isSelectMode = !isSelectMode
        selectedItems.clear() // Clear selected items when switching modes
        notifyDataSetChanged() // Refresh the entire list
    }
    fun getSelectedImagePaths(): ArrayList<String> {
        selected_Item_Send.clear()
        Log.e("GET_SELECTED_IMAGES", "onBindViewHolder:>>$selectedItems", )
        for (i in selectedItems){
            Log.e("GET_SELECTED_IMAGES", "onBindViewHolder:${mediaItems.get(i).path} ")
            selected_Item_Send.add(mediaItems.get(i).path.toString())
        }
        Log.e("GET_SELECTED_IMAGES", "onBindViewHolder:${selected_Item_Send} ")
        return selected_Item_Send

    }
    fun selectItems(): ArrayList<Int> {
        selectItemIndex.clear()
        for (i in selectedItems){
            selectItemIndex.add(i)
        }
        return selectItemIndex
    }
}
