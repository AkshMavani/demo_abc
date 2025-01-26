package com.example.gallery.util;

import static com.example.gallery.util.Video_Recently_Utils.getCount;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.Observable;
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


    /* JADX INFO: Access modifiers changed from: package-private */
    public static List<AlbumDetail> getListAlbum(Context context) {
        List<AlbumDetail> albumList = new ArrayList<>();
        Uri contentUri;

        try {
            Bundle bundle = new Bundle();
            bundle.putString("android:query-arg-sql-sort-order", "date_modified DESC");
            bundle.putString("android:query-arg-sql-group-by", "bucket_display_name");

            if (Build.VERSION.SDK_INT >= 30) {
                contentUri = MediaStore.Files.getContentUri("external_primary");
            } else {
                contentUri = MediaStore.Files.getContentUri("external");
            }

            Cursor query = Build.VERSION.SDK_INT >= 26
                    ? context.getContentResolver().query(contentUri, null, bundle, null)
                    : null;

            if (query != null) {
                int count = query.getCount();

                for (int i = 0; i < count; i++) {
                    AlbumDetail albumDetail = new AlbumDetail();
                    query.moveToPosition(i);

                    @SuppressLint("Range") String path = query.getString(query.getColumnIndex("_data"));
                    @SuppressLint("Range") String bucketName = query.getString(query.getColumnIndex("bucket_display_name"));
                    @SuppressLint("Range") int bucketId = query.getInt(query.getColumnIndex("bucket_id"));

                    albumDetail.setPath(path);
                    albumDetail.setBuget_name(bucketName);
                    albumDetail.setBugetId(bucketId);
                    albumDetail.setCount(getCount(context, bucketName));

                    if (path.contains("mp4") || path.contains("jpeg") || path.contains("jpg") || path.contains("png")) {
                        albumList.add(albumDetail);
                    }
                }

                query.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return albumList;
    }

    private static int getCount(Context context, String bucketName) {
        int count = 0;
        Uri contentUri = MediaStore.Files.getContentUri("external");

        String selection = "bucket_display_name = ?";
        String[] selectionArgs = new String[]{bucketName};

        Cursor cursor = context.getContentResolver().query(contentUri, null, selection, selectionArgs, null);

        if (cursor != null) {
            count = cursor.getCount();
            cursor.close();
        }

        return count;
    }
}
