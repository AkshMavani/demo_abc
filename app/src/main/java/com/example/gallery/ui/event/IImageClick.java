package com.example.gallery.ui.event;

import com.example.gallery.ui.model.GalleryModel;

public interface IImageClick {
    void add(int pos, GalleryModel galleryModel);

    void remove(int pos, GalleryModel galleryModel);
}
