package com.example.gallery.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;

import com.example.gallery.ui.model.AlbumDetail;
import com.example.gallery.ui.model.GalleryModel;


import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/* loaded from: classes2.dex */
public class AlbumUtil {
    public static boolean isImageFile(String path) {
        String guessContentTypeFromName = URLConnection.guessContentTypeFromName(path);
        return guessContentTypeFromName != null && (guessContentTypeFromName.startsWith("image") || guessContentTypeFromName.startsWith("video"));
    }


    /* JADX INFO: Access modifiers changed from: package-private */


    public static List<GalleryModel> getAlbumNames(List<GalleryModel> galleryModels, String bugetId) {
        ArrayList arrayList = new ArrayList();
        if (galleryModels != null && galleryModels.size() > 0) {
            for (int i = 0; i < galleryModels.size(); i++) {
                if (galleryModels.get(i).getPath() != null && galleryModels.get(i).getBucketDisplayName().equals(bugetId)) {
                    arrayList.add(galleryModels.get(i));
                }
            }
        }
        return arrayList;
    }

    public static int getCountALbum(List<GalleryModel> galleryModels, String bugetId) {
        if (galleryModels == null || galleryModels.size() <= 0) {
            return 0;
        }
        int i = 0;
        for (int i2 = 0; i2 < galleryModels.size(); i2++) {
            if (galleryModels.get(i2).getPath() != null && galleryModels.get(i2).getBucketDisplayName().equals(bugetId)) {
                i++;
            }
        }
        return i;
    }

}
