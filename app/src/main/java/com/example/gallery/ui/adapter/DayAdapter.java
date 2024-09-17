package com.example.gallery.ui.adapter;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.lifecycle.ViewModel;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.demo_full.R;
import com.example.gallery.ui.MediaViewModel;
import com.example.gallery.ui.model.GalleryModel;
import com.example.gallery.util.DateUtils;
import com.example.gallery.util.StickyHeaderGridAdapter;

import java.util.List;

/* loaded from: classes.dex */
public class DayAdapter extends StickyHeaderGridAdapter {
    Context context;
    List<List<GalleryModel>> galleryDaylist;
    boolean isChoose;
    MediaViewModel mhomeViewModel;
    OnClickCustom onClickCustom;
    private final SparseBooleanArray selectedItems = new SparseBooleanArray();

    /* loaded from: classes.dex */
    public interface OnClickCustom {
        void onCLick1(View view, GalleryModel galleryModel);
    }

    public DayAdapter(MediaViewModel homeViewModel, Context context, List<List<GalleryModel>> galleryDaylist, OnClickCustom onClickCustom) {
        this.context = context;
        this.galleryDaylist = galleryDaylist;
        this.onClickCustom = onClickCustom;
        this.mhomeViewModel = homeViewModel;
    }

    public void setChoose(boolean choose) {
        this.isChoose = choose;
        this.galleryDaylist.size();
        for (int i = 0; i < this.galleryDaylist.size(); i++) {
            for (int i2 = 0; i2 < this.galleryDaylist.get(i).size(); i2++) {
                this.galleryDaylist.get(i).get(i2).setChoose(false);
            }
        }
        notifyDataSetChanged();
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public int getSectionCount() {
        return this.galleryDaylist.size();
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public int getSectionItemCount(int section) {
        return this.galleryDaylist.get(section).size() >= 4 ? 4 : 1;
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public MyHeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent, int headerType) {
        return new MyHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.view_header, parent, false));
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public StickyHeaderGridAdapter.ItemViewHolder onCreateItemViewHolder(ViewGroup parent, int itemType) {
        return new MyItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day, parent, false), this.context);
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public void onBindHeaderViewHolder(StickyHeaderGridAdapter.HeaderViewHolder viewHolder, int section) {
        MyHeaderViewHolder myHeaderViewHolder = (MyHeaderViewHolder) viewHolder;
        if (section <= this.galleryDaylist.size() - 1) {
            myHeaderViewHolder.tx_header.setText(DateUtils.INSTANCE.formatdateImage(this.galleryDaylist.get(section).get(0).getDatetaken()));
        }
    }

    @Override // com.example.iphoto.view.StickyHeaderGridAdapter
    public void onBindItemViewHolder(StickyHeaderGridAdapter.ItemViewHolder viewHolder, int section, int offset) {
        MyItemViewHolder myItemViewHolder = (MyItemViewHolder) viewHolder;
        if (section <= this.galleryDaylist.size() - 1) {
            Glide.with(this.context).load(this.galleryDaylist.get(section).get(offset).getPath()).diskCacheStrategy(DiskCacheStrategy.NONE).into(myItemViewHolder.mImageView);
            if (offset == 0) {
                if (this.isChoose) {
                    myItemViewHolder.btnSelect.setVisibility(View.VISIBLE);
                } else {
                    myItemViewHolder.btnSelect.setVisibility(View.GONE);
                }
                myItemViewHolder.tvDateTime.setVisibility(View.VISIBLE);
                myItemViewHolder.tvDateTime.setText(DateUtils.INSTANCE.formatdateImage(this.galleryDaylist.get(section).get(offset).getDatetaken()));
            } else {
                myItemViewHolder.tvDateTime.setVisibility(View.GONE);
                myItemViewHolder.btnSelect.setVisibility(View.GONE);
            }
            if (offset == 3) {
                myItemViewHolder.tv_count.setVisibility(View.VISIBLE);
                myItemViewHolder.tv_count.setText("+" + (this.galleryDaylist.get(section).size() - 4));
            } else {
                myItemViewHolder.tv_count.setVisibility(View.GONE);
            }
            if (this.galleryDaylist.get(section).size() < 4 && offset == 0) {
                myItemViewHolder.tv_count.setText("+" + (this.galleryDaylist.get(section).size() - 1));
                myItemViewHolder.tv_count.setVisibility(View.VISIBLE);
            }
            if (this.galleryDaylist.get(section).size() == 1 || this.galleryDaylist.get(section).size() == 4) {
                myItemViewHolder.tv_count.setVisibility(View.GONE);
            }
            if (this.galleryDaylist.get(section).get(offset).getChoose()) {
                myItemViewHolder.rl_view.setVisibility(View.VISIBLE);
                myItemViewHolder.icon_video.setVisibility(View.GONE);
            } else {
                myItemViewHolder.rl_view.setVisibility(View.GONE);
            }
        }
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
        TextView btnSelect;
        TextView icon_video;
        ImageView mImageView;
        RelativeLayout rl_view;
        TextView tvDateTime;
        TextView tv_count;

        public MyItemViewHolder(View itemView, Object p1) {
            super(itemView);
            this.btnSelect = (TextView) itemView.findViewById(R.id.btnSelect);
            this.tv_count = (TextView) itemView.findViewById(R.id.tv_count);
            this.tvDateTime = (TextView) itemView.findViewById(R.id.tvDateTime);
            this.rl_view = (RelativeLayout) itemView.findViewById(R.id.rl_view);
            this.icon_video = (TextView) itemView.findViewById(R.id.icon_video);
            this.mImageView = (ImageView) itemView.findViewById(R.id.preview_image_view);
            this.btnSelect.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.DayAdapter.MyItemViewHolder.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int adapterPositionSection = DayAdapter.this.getAdapterPositionSection(MyItemViewHolder.this.getAdapterPosition());
                    if (MyItemViewHolder.this.btnSelect.getText().toString().equals(DayAdapter.this.context.getString(R.string.select_all))) {
                        for (int i = 0; i < DayAdapter.this.galleryDaylist.get(adapterPositionSection).size(); i++) {
                            DayAdapter.this.galleryDaylist.get(adapterPositionSection).get(i).setChoose(true);
                        }
                        MyItemViewHolder.this.btnSelect.setText(DayAdapter.this.context.getString(R.string.unselect_all));
                        DayAdapter.this.notifyDataSetChanged();
                        return;
                    }
                    if (MyItemViewHolder.this.btnSelect.getText().toString().equals(DayAdapter.this.context.getString(R.string.unselect_all))) {
                        for (int i2 = 0; i2 < DayAdapter.this.galleryDaylist.get(adapterPositionSection).size(); i2++) {
                            DayAdapter.this.galleryDaylist.get(adapterPositionSection).get(i2).setChoose(false);
                        }
                        MyItemViewHolder.this.btnSelect.setText(DayAdapter.this.context.getString(R.string.select_all));
                        DayAdapter.this.notifyDataSetChanged();
                    }
                }
            });
            this.mImageView.setOnClickListener(new View.OnClickListener() { // from class: com.example.iphoto.adapter.DayAdapter.MyItemViewHolder.2
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    int adapterPositionSection = DayAdapter.this.getAdapterPositionSection(MyItemViewHolder.this.getAdapterPosition());
                    int itemSectionOffset = DayAdapter.this.getItemSectionOffset(adapterPositionSection, MyItemViewHolder.this.getAdapterPosition());
                    if (DayAdapter.this.isChoose) {
                        if (DayAdapter.this.galleryDaylist.get(adapterPositionSection).get(itemSectionOffset).getChoose()) {
                            DayAdapter.this.galleryDaylist.get(adapterPositionSection).get(itemSectionOffset).setChoose(false);
                        } else {
                            DayAdapter.this.galleryDaylist.get(adapterPositionSection).get(itemSectionOffset).setChoose(true);
                        }
                        DayAdapter.this.notifyDataSetChanged();
                        return;
                    }
                    DayAdapter.this.onClickCustom.onCLick1(view, DayAdapter.this.galleryDaylist.get(adapterPositionSection).get(itemSectionOffset));
                }
            });
        }
    }
    public boolean isHeader(int section) {
        return section == 0;
    }

}
