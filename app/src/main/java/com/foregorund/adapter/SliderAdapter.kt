package com.foregorund.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2

import com.example.demo_full.R

class SliderAdapter internal constructor(sliderItems: List<slider>, viewPager2: ViewPager2, var context: Context, var click:interface_click) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {
    private val sliderItems: List<slider>
    private val viewPager2: ViewPager2

    init {
        this.sliderItems = sliderItems
        this.viewPager2 = viewPager2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return SliderViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.slider_item_container , parent, false)
        )
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.setImage(sliderItems[position])
        holder.itemView.setOnClickListener {
            viewPager2.visibility= View.GONE
            val sp=context.getSharedPreferences("SP", Context.MODE_PRIVATE)
            val edit=sp.edit()
            edit.putInt("img",sliderItems[position].img).apply()
            click.click_item(sliderItems[position].img)
        }
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }

    inner class SliderViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView

        init {
            imageView = itemView.findViewById(R.id.img)
        }

        fun setImage(sliderItems: slider) {

            imageView.setImageResource(sliderItems.img)
        }
    }
}