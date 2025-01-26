package com.example.gallery.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo_full.R;
import com.example.gallery.ui.event.IImageClick;
import com.example.gallery.ui.model.GalleryModel;

import java.util.Iterator;
import java.util.List;

public class AddImageAdapter extends RecyclerView.Adapter<AddImageAdapter.AddImageHolder> {
    Context context;
    List<GalleryModel> galleryModels;
    IImageClick iImageClick;

    public AddImageAdapter(List<GalleryModel> galleryModels, Context context, IImageClick iImageClick) {
        this.galleryModels = galleryModels;
        this.context = context;
        this.iImageClick = iImageClick;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public AddImageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddImageHolder(LayoutInflater.from(this.context).inflate(R.layout.item_image1, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(AddImageHolder holder, int position) {
        GalleryModel galleryModel = this.galleryModels.get(position);
        Glide.with(this.context).load(galleryModel.getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.img_thumb);
        if (galleryModel.getPath().contains(".mp4")) {
            holder.tv_time_length.setVisibility(View.VISIBLE);
            holder.tv_time_length.setText(galleryModel.getDuration());
        } else {
            holder.tv_time_length.setVisibility(View.INVISIBLE);
        }
        if (galleryModel.getChoose()) {
            holder.v_select.setVisibility(View.VISIBLE);
            holder.img_ic_select.setVisibility(View.VISIBLE);
        } else {
            holder.v_select.setVisibility(View.INVISIBLE);
            holder.img_ic_select.setVisibility(View.INVISIBLE);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.galleryModels.size();
    }

    /* loaded from: classes.dex */
    public class AddImageHolder extends RecyclerView.ViewHolder {
        ImageView imgFavorite;
        ImageView img_ic_select;
        ImageView img_thumb;
        TextView tv_time_length;
        View v_select;

        public AddImageHolder(View itemView) {
            super(itemView);
            this.img_thumb = (ImageView) itemView.findViewById(R.id.img_thumb);
            this.img_ic_select = (ImageView) itemView.findViewById(R.id.img_ic_select);
            this.imgFavorite = (ImageView) itemView.findViewById(R.id.imgFavorite);
            this.tv_time_length = (TextView) itemView.findViewById(R.id.tv_time_length);
            this.v_select = itemView.findViewById(R.id.v_select);
            itemView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.AddImageAdapter.AddImageHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (AddImageHolder.this.getAdapterPosition() < 0) {
                        return;
                    }
                    if (AddImageAdapter.this.galleryModels.get(AddImageHolder.this.getAdapterPosition()).getChoose()) {
                        AddImageAdapter.this.galleryModels.get(AddImageHolder.this.getAdapterPosition()).setChoose(false);
                        AddImageAdapter.this.iImageClick.remove(AddImageHolder.this.getAdapterPosition(), AddImageAdapter.this.galleryModels.get(AddImageHolder.this.getAdapterPosition()));
                    } else {
                        AddImageAdapter.this.galleryModels.get(AddImageHolder.this.getAdapterPosition()).setChoose(true);
                        AddImageAdapter.this.iImageClick.add(AddImageHolder.this.getAdapterPosition(), AddImageAdapter.this.galleryModels.get(AddImageHolder.this.getAdapterPosition()));
                    }
                    AddImageAdapter.this.notifyDataSetChanged();
                }
            });
        }
    }

    public void clearItem() {
        Iterator<GalleryModel> it = this.galleryModels.iterator();
        while (it.hasNext()) {
            it.next().setChoose(false);
        }
    }
}