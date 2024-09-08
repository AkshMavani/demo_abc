package com.example.gallery.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo_full.R;
import com.example.gallery.ui.model.GalleryModel;
import com.example.gallery.util.DateUtils;
import com.example.gallery.util.StickyHeaderGridAdapter;

import java.util.List;

/* loaded from: classes.dex */
public class MonthAdapter extends StickyHeaderGridAdapter {
    Context context;
    List<List<GalleryModel>> galleryDaylist;
    OnClickCustom onClickCustom;

    /* loaded from: classes.dex */
    public interface OnClickCustom {
        void onCLick1(GalleryModel galleryModel);
    }

    public MonthAdapter(Context context, List<List<GalleryModel>> galleryDaylist, OnClickCustom onClickCustom) {
        this.context = context;
        this.galleryDaylist = galleryDaylist;
        this.onClickCustom = onClickCustom;
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public int getSectionCount() {
        return this.galleryDaylist.size();
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public int getSectionItemCount(int section) {
        return this.galleryDaylist.get(section).size();
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public MyHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        return new MyHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_month, parent, false));
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public StickyHeaderGridAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        return new MyItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_month, parent, false), this.context);
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public void onBindHeaderViewHolder(StickyHeaderGridAdapter.HeaderViewHolder viewHolder, int section) {
        StringBuilder sb = new StringBuilder(DateUtils.INSTANCE.formatdateMonth(this.galleryDaylist.get(section).get(0).getDatetaken()) + "");
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        ((MyHeaderViewHolder) viewHolder).tx_header.setText(sb.toString());
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public void onBindItemViewHolder(StickyHeaderGridAdapter.ItemViewHolder viewHolder, int section, int offset) {
        MyItemViewHolder myItemViewHolder = (MyItemViewHolder) viewHolder;
        Glide.with(this.context).load(this.galleryDaylist.get(section).get(offset).getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).into(myItemViewHolder.mImageView);
        myItemViewHolder.tvDateTime.setText(DateUtils.INSTANCE.formatdateMonthItem(this.galleryDaylist.get(section).get(offset).getDatetaken()));
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class MyHeaderViewHolder extends StickyHeaderGridAdapter.HeaderViewHolder {
        private TextView tx_header;

        public MyHeaderViewHolder(View view) {
            super(view);
            this.tx_header = (TextView) this.itemView.findViewById(R.id.tv_header);
        }
    }

    /* loaded from: classes.dex */
    private class MyItemViewHolder extends StickyHeaderGridAdapter.ItemViewHolder {
        ImageView mImageView;
        TextView tvDateTime;

        public MyItemViewHolder(View itemView, Object p1) {
            super(itemView);
            this.tvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            ImageView imageView = (ImageView) itemView.findViewById(R.id.preview_image_view);
            this.mImageView = imageView;
            imageView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.MonthAdapter.MyItemViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int adapterPositionSection = MonthAdapter.this.getAdapterPositionSection(MyItemViewHolder.this.getAdapterPosition());
                    MonthAdapter.this.onClickCustom.onCLick1(MonthAdapter.this.galleryDaylist.get(adapterPositionSection).get(MonthAdapter.this.getItemSectionOffset(adapterPositionSection, MyItemViewHolder.this.getAdapterPosition())));
                }
            });
        }
    }
}
