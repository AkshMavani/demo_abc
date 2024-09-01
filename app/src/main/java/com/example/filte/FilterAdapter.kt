package com.example.filte

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.demo_full.R

class FilterAdapter(private val mList: List<Model_Filter>) : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view_design_filter, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]
        holder.imageView.setImageResource(R.drawable.ic_launcher_background)
        holder.textView.text=ItemsViewModel.filters.name.toString()
        if (position==1){
            holder.imageView.setImageResource(R.drawable.aa1)
        }
        if (position==2){
            holder.imageView.setImageResource(R.drawable.a22)
        }
        if (position==3){
            holder.imageView.setImageResource(R.drawable.a4)
        }
        if (position==4){
            holder.imageView.setImageResource(R.drawable.a5)
        }
        if (position==5){
            holder.imageView.setImageResource(R.drawable.a6)
        }
        if (position==6){
            holder.imageView.setImageResource(R.drawable.a7)
        }
         if (position==7){
             holder.imageView.setImageResource(R.drawable.a8)
         }
         if (position==8){
             holder.imageView.setImageResource(R.drawable.a9)
         }
         if (position==9){
             holder.imageView.setImageResource(R.drawable.a10)
         }
         if (position==10){
             holder.imageView.setImageResource(R.drawable.a11)
         }
         if (position==11){
             holder.imageView.setImageResource(R.drawable.a12)
         }
          if (position==12){
                     holder.imageView.setImageResource(R.drawable.a13)
          }
          if (position==13){
                     holder.imageView.setImageResource(R.drawable.a14)
          }
          if (position==14){
                     holder.imageView.setImageResource(R.drawable.a15)
          }
        if (position==15){
                     holder.imageView.setImageResource(R.drawable.a16)
          }
        if (position==16){
                     holder.imageView.setImageResource(R.drawable.a17)
          }
        if (position==17){
            holder.imageView.setImageResource(R.drawable.a18)
          }
         if (position==18){
             holder.imageView.setImageResource(R.drawable.a19)
         }

         if (position==19){
             holder.imageView.setImageResource(R.drawable.a20)
         }
         if (position==20){
             holder.imageView.setImageResource(R.drawable.a21)
         }
         if (position==21){
             holder.imageView.setImageResource(R.drawable.a4)
         }


    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imgView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }
}