package com.example.gallery.ui.adapter;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo_full.R;
import com.example.gallery.ui.MediaItem;
import com.example.gallery.ui.model.GalleryModel;
import com.example.gallery.util.DateUtils;


import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class YearAdapter extends RecyclerView.Adapter<YearAdapter.YearHolder> {
    Activity context;
    private ArrayList<GalleryModel> galleryModelList;
    OnClickCuston on;

    /* loaded from: classes.dex */
    public interface OnClickCuston {
        void onCLick1(GalleryModel galleryModel);
    }

    public YearAdapter(Activity context, ArrayList<GalleryModel> list, OnClickCuston onClickCuston) {
        this.context = context;
        this.galleryModelList = list;
        this.on = onClickCuston;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public YearHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new YearHolder(this.context.getLayoutInflater().inflate(R.layout.item_year, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(YearHolder holder, int position) {
        Log.e("TAG_TTT", "onBindViewHolder: "+galleryModelList.get(position));
        holder.tvDateTime.setText(DateUtils.INSTANCE.formatdateYear(galleryModelList.get(position).getDatetaken()));
        Glide.with(this.context).load(this.galleryModelList.get(position).getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.mImageView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.galleryModelList.size();
    }

    /* loaded from: classes.dex */
    public class YearHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        TextView tvDateTime;

        public YearHolder(View view) {
            super(view);
            this.tvDateTime = (TextView) view.findViewById(R.id.tvDateTime);
            ImageView imageView = (ImageView) view.findViewById(R.id.preview_image_view);
            this.mImageView = imageView;
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.YearAdapter.YearHolder.1
                @Override
                public void onClick(View view2) {
                    YearAdapter.this.on.onCLick1((GalleryModel) YearAdapter.this.galleryModelList.get(YearHolder.this.getAdapterPosition()));
                }
            });
        }
    }

}
