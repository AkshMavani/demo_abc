package com.example.language

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R
import com.example.filte.Model_Filter
interface click_language{
    fun click(position: Int,name:String)
}
class Language_Adapter(private val mList: ArrayList<Model_Language>,var clickLanguage: click_language) : RecyclerView.Adapter<Language_Adapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_language, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.textView.text=ItemsViewModel.name
        holder.imageView.setImageResource(ItemsViewModel.img)
        holder.itemView.setOnClickListener {
            clickLanguage.click(position,ItemsViewModel.name)
        }

    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.img_language)
        val textView: TextView = itemView.findViewById(R.id.txt_language)
    }
}