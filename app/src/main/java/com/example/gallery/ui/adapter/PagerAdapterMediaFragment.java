package com.example.gallery.ui.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.gallery.FragmentMediaPager;
import com.example.gallery.ui.model.GalleryModel;
import com.example.gallery.util.IVideoUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PagerAdapterMediaFragment extends FragmentStatePagerAdapter {
    Fragment fragment;
    public List<GalleryModel> galleryModels;
    IVideoUpdate iVideoUpdate;
    HashMap<Integer, FragmentMediaPager> map;

    public PagerAdapterMediaFragment(Fragment fragment, List<GalleryModel> galleryModels, IVideoUpdate iVideoUpdate) {
        super(fragment.getChildFragmentManager());
        this.galleryModels = new ArrayList();
        this.map = new HashMap<>();
        this.iVideoUpdate = iVideoUpdate;
        this.galleryModels = galleryModels;
        this.fragment = fragment;
    }

    @Override // androidx.fragment.app.FragmentStatePagerAdapter
    public Fragment getItem(int position) {
        return FragmentMediaPager.newInstance(this.galleryModels, position, this.fragment, this.iVideoUpdate);
    }

    @Override // androidx.viewpager.widget.PagerAdapter
    public int getCount() {
        return this.galleryModels.size();
    }
}

