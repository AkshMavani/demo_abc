package com.example.gallery.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.demo_full.R;
import com.example.gallery.ui.MediaViewModel;
import com.example.gallery.ui.model.AlbumDetail;
import com.example.gallery.util.Video_Recently_Utils;

import java.util.List;

public class AlbumsGeneralAdapter extends RecyclerView.Adapter<AlbumsGeneralAdapter.AlbumsGeneralHolder> {
    List<AlbumDetail> albumDetailList;
    Context context;
    boolean ischeck;
    MediaViewModel mhomeViewModel;
    OnClickCustom onClickCustom;

    /* loaded from: classes.dex */
    public interface OnClickCustom {
        void onCLick1(AlbumDetail albumDetail);

        void remove(AlbumDetail albumDetail);
    }

    public AlbumsGeneralAdapter(MediaViewModel homeViewModel, List<AlbumDetail> albumDetailList, Context context, OnClickCustom onClickCustom) {
        this.albumDetailList = albumDetailList;
        this.context = context;
        this.onClickCustom = onClickCustom;
        this.mhomeViewModel = homeViewModel;
    }

    public void setIscheck(boolean ischeck) {
        this.ischeck = ischeck;
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public AlbumsGeneralHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AlbumsGeneralHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_albums_general, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(AlbumsGeneralHolder holder, int position) {
        AlbumDetail albumDetail = this.albumDetailList.get(position);
        Log.e("POSTITTION12", "onBindViewHolder: "+position);
        Glide.with(this.context).load(albumDetail.getPath()).apply((BaseRequestOptions<?>) new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(10))).diskCacheStrategy(DiskCacheStrategy.NONE).into(holder.img_thumb);
        holder.tv_name.setText(albumDetail.getBuget_name());
      //  holder.tv_total.setText(Video_Recently_Utils.getCountALbum(mGalleryModels.getValue(), String.valueOf(albumDetail.getBuget_name())) + "");
        if (this.ischeck) {
            holder.imgRemove.setVisibility(0);
        } else {
            holder.imgRemove.setVisibility(8);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.albumDetailList.size();
    }

    /* loaded from: classes.dex */
    public class AlbumsGeneralHolder extends RecyclerView.ViewHolder {
        ImageView imgRemove;
        SquareImage img_thumb;
        TextView tv_name;
        TextView tv_total;

        public AlbumsGeneralHolder(View itemView) {
            super(itemView);
            this.tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            this.tv_total = (TextView) itemView.findViewById(R.id.tv_total);
            this.img_thumb = (SquareImage) itemView.findViewById(R.id.img_thumb);
            this.imgRemove = (ImageView) itemView.findViewById(R.id.imgRemove);
            itemView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.AlbumsGeneralAdapter.AlbumsGeneralHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    if (AlbumsGeneralAdapter.this.ischeck) {
                        return;
                    }
                    AlbumsGeneralAdapter.this.onClickCustom.onCLick1(AlbumsGeneralAdapter.this.albumDetailList.get(AlbumsGeneralHolder.this.getAdapterPosition()));
                }
            });
            this.imgRemove.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.AlbumsGeneralAdapter.AlbumsGeneralHolder.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    AlbumsGeneralAdapter.this.onClickCustom.remove(AlbumsGeneralAdapter.this.albumDetailList.get(AlbumsGeneralHolder.this.getAdapterPosition()));
                }
            });
        }
    }
}
