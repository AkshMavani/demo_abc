package com.example.demo_full

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

//class FolderAdapter(private val folders: List<String>, private val context: Context) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {
//
//    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val folderName: TextView = view.findViewById(R.id.folderName)
//
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_folder, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.bind(folders[position])
//    }
//
//    override fun getItemCount() = folders.size
//}

interface foulder_click{
    fun click(name:String)
}

class FolderAdapter(private val mList: List<Triple<String, String, Long>>, private val context: Context,
                    var foulderClick: foulder_click) : RecyclerView.Adapter<FolderAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_folder, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.textView.text=ItemsViewModel.first
        Glide.with(context).load(ItemsViewModel.second).centerCrop().placeholder(R.drawable.ic_launcher_background).into(holder.img);

        holder.itemView.setOnClickListener {
            Log.e("FLP", "onBindViewHolder:$ItemsViewModel ", )
//            val intent=Intent(context,MainActivity4::class.java)
//            intent.putExtra("Foulder",ItemsViewModel.first)
//            context.startActivity(intent)
            foulderClick.click(ItemsViewModel.first)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val textView: TextView = itemView.findViewById(R.id.folderName)
        val img: ImageView = itemView.findViewById(R.id.img)

    }
}