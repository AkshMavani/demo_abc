package com.example.gallery.ui.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.demo_full.R;
import com.example.gallery.ui.model.GalleryModel;


import java.util.List;

/* loaded from: classes.dex */
public class ThumbnailAdapter extends RecyclerView.Adapter<ThumbnailAdapter.ThumbnailApdater> {
    Context context;
    List<GalleryModel> galleryModels;
    OnClickCustom onClickCustom;

    /* loaded from: classes.dex */
    public interface OnClickCustom {
        void onCLick1(int index);
    }

    public void selectPos(int pos) {
    }

    public ThumbnailAdapter(Context context, List<GalleryModel> galleryModels, OnClickCustom onClickCustom) {
        this.galleryModels = galleryModels;
        this.context = context;
        this.onClickCustom = onClickCustom;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ThumbnailApdater onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ThumbnailApdater(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_thumb, parent, false));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(final ThumbnailApdater holder, int position) {
        final GalleryModel galleryModel = this.galleryModels.get(position);
        if (galleryModel.getUri() != null) {
            Glide.with(this.context).load(Uri.parse(galleryModel.getUri())).listener(new RequestListener<Drawable>() { // from class: com.example.iphoto.adapter.ThumbnailAdapter.1
                @Override // com.bumptech.glide.request.RequestListener
                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                    return false;
                }

                @Override // com.bumptech.glide.request.RequestListener
                public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                    holder.mImageView.post(new Runnable() { // from class: com.example.iphoto.adapter.ThumbnailAdapter.1.1
                        @Override // java.lang.Runnable
                        public void run() {
                            Glide.with(ThumbnailAdapter.this.context).load(galleryModel.getPath()).into(holder.mImageView);
                        }
                    });
                    return false;
                }
            }).into(holder.mImageView);
        } else {
            Glide.with(this.context).load(galleryModel.getPath()).into(holder.mImageView);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.galleryModels.size();
    }

    /* loaded from: classes.dex */
    public class ThumbnailApdater extends RecyclerView.ViewHolder {
        ImageView mImageView;

        public ThumbnailApdater(View itemView) {
            super(itemView);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.preview_image_view);
            this.mImageView = imageView;
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.ThumbnailAdapter.ThumbnailApdater.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ThumbnailAdapter.this.onClickCustom.onCLick1(ThumbnailApdater.this.getAdapterPosition());
                }
            });
        }
    }
}
